import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A generic, thread-safe cache with pluggable eviction strategies and built-in statistics.
 * <p>
 * Thread safety is achieved using a {@link ReadWriteLock}: reads (get, contains, size)
 * acquire the read lock, while writes (put, remove, clear) acquire the write lock.
 * This allows concurrent reads while ensuring exclusive access during mutations.
 * <p>
 * The eviction strategy is selected at configuration time via {@link CacheConfig} and
 * determines which entry is removed when the cache reaches its maximum size.
 * <p>
 * Example usage:
 * <pre>{@code
 * CacheConfig<String> config = CacheConfig.<String>builder()
 *     .maxSize(1000)
 *     .evictionType(CacheConfig.EvictionType.LRU)
 *     .build();
 *
 * Cache<String, MyObject> cache = new Cache<>(config);
 * cache.put("key1", myObject);
 * Optional<MyObject> result = cache.get("key1");
 * }</pre>
 *
 * @param <K> the type of keys
 * @param <V> the type of values
 */
public class Cache<K, V> {

    private final Map<K, V> store;
    private final EvictionStrategy<K> evictionStrategy;
    private final int maxSize;
    private final CacheStatistics statistics;
    private final ReadWriteLock lock;

    /**
     * Creates a new cache with the given configuration.
     *
     * @param config the cache configuration specifying max size and eviction strategy
     */
    public Cache(CacheConfig<K> config) {
        this.maxSize = config.getMaxSize();
        this.evictionStrategy = config.getEvictionStrategy();
        this.store = new ConcurrentHashMap<>(maxSize);
        this.statistics = new CacheStatistics();
        this.lock = new ReentrantReadWriteLock();
    }

    /**
     * Retrieves the value associated with the given key.
     * Records a hit or miss in the statistics and notifies the eviction strategy on access.
     *
     * @param key the key to look up
     * @return an {@link Optional} containing the value if present, or empty if not found
     */
    public Optional<V> get(K key) {
        lock.readLock().lock();
        try {
            V value = store.get(key);
            if (value != null) {
                // Check TTL expiration if applicable
                if (evictionStrategy instanceof TTLEvictionStrategy) {
                    TTLEvictionStrategy<K> ttlStrategy = (TTLEvictionStrategy<K>) evictionStrategy;
                    if (ttlStrategy.isExpired(key)) {
                        // Need write lock to remove — upgrade by releasing read first
                        lock.readLock().unlock();
                        lock.writeLock().lock();
                        try {
                            // Re-check after acquiring write lock
                            if (store.containsKey(key) && ttlStrategy.isExpired(key)) {
                                store.remove(key);
                                evictionStrategy.onRemove(key);
                                statistics.recordEviction();
                            }
                            statistics.recordMiss();
                            return Optional.empty();
                        } finally {
                            lock.readLock().lock(); // Downgrade
                            lock.writeLock().unlock();
                        }
                    }
                }
                evictionStrategy.onAccess(key);
                statistics.recordHit();
                return Optional.of(value);
            } else {
                statistics.recordMiss();
                return Optional.empty();
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Inserts or updates a key-value pair in the cache.
     * If the cache is at capacity and the key is new, the eviction strategy
     * selects an entry to remove before inserting.
     *
     * @param key   the key
     * @param value the value
     */
    public void put(K key, V value) {
        lock.writeLock().lock();
        try {
            // If updating an existing key, no need to evict
            if (store.containsKey(key)) {
                store.put(key, value);
                evictionStrategy.onPut(key);
                return;
            }

            // Evict if at capacity
            if (store.size() >= maxSize) {
                evictEntries();
            }

            store.put(key, value);
            evictionStrategy.onPut(key);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Removes the entry with the given key from the cache.
     *
     * @param key the key to remove
     * @return an {@link Optional} containing the removed value, or empty if the key was not present
     */
    public Optional<V> remove(K key) {
        lock.writeLock().lock();
        try {
            V value = store.remove(key);
            if (value != null) {
                evictionStrategy.onRemove(key);
                return Optional.of(value);
            }
            return Optional.empty();
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Checks whether the cache contains the given key.
     * This does not count as an access for eviction purposes and does not
     * affect statistics.
     *
     * @param key the key to check
     * @return true if the key is present in the cache
     */
    public boolean containsKey(K key) {
        lock.readLock().lock();
        try {
            return store.containsKey(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Returns the current number of entries in the cache.
     *
     * @return the cache size
     */
    public int size() {
        lock.readLock().lock();
        try {
            return store.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Removes all entries from the cache and resets the eviction strategy.
     * Statistics are preserved; call {@link CacheStatistics#reset()} on the
     * returned statistics object to clear them.
     */
    public void clear() {
        lock.writeLock().lock();
        try {
            store.clear();
            evictionStrategy.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Returns the cache statistics tracker. The returned object is thread-safe
     * and provides a live view of the statistics.
     *
     * @return the cache statistics
     */
    public CacheStatistics getStatistics() {
        return statistics;
    }

    /**
     * Returns the maximum number of entries this cache can hold.
     *
     * @return the configured max size
     */
    public int getMaxSize() {
        return maxSize;
    }

    /**
     * Evicts entries until there is room for at least one new entry.
     * If using TTL strategy, expired entries are purged first.
     * Must be called while holding the write lock.
     */
    private void evictEntries() {
        // For TTL strategy, purge all expired entries first
        if (evictionStrategy instanceof TTLEvictionStrategy) {
            TTLEvictionStrategy<K> ttlStrategy = (TTLEvictionStrategy<K>) evictionStrategy;
            Map<K, Long> expired = ttlStrategy.removeExpired();
            for (K expiredKey : expired.keySet()) {
                store.remove(expiredKey);
                statistics.recordEviction();
            }
        }

        // If still at capacity, evict according to strategy
        while (store.size() >= maxSize) {
            K victim = evictionStrategy.evict();
            if (victim == null) {
                break; // No more candidates
            }
            store.remove(victim);
            statistics.recordEviction();
        }
    }
}
