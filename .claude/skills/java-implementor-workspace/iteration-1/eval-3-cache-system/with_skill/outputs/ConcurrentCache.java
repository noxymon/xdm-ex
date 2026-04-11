import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A thread-safe, bounded cache that delegates eviction decisions to a
 * pluggable {@link EvictionStrategy}.
 *
 * <p>Thread safety is achieved via a {@link ReadWriteLock}: reads acquire
 * the read lock (allowing concurrent reads) and writes acquire the write lock.
 * Statistics counters use atomic variables to avoid contention on reads.
 *
 * <p>Instances should be created via {@link CacheBuilder} rather than
 * calling the constructor directly.
 *
 * @param <K> the type of keys maintained by this cache
 * @param <V> the type of mapped values
 */
public final class ConcurrentCache<K, V> implements Cache<K, V> {

    private final int maxSize;
    private final EvictionStrategy<K> strategy;
    private final Map<K, V> store;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private final AtomicLong hitCount = new AtomicLong();
    private final AtomicLong missCount = new AtomicLong();
    private final AtomicLong evictionCount = new AtomicLong();

    /**
     * Creates a cache with the given maximum size and eviction strategy.
     *
     * <p>Prefer using {@link CacheBuilder} for construction.
     *
     * @param maxSize  the maximum number of entries, must be at least 1
     * @param strategy the eviction strategy, must not be null
     * @throws IllegalArgumentException if maxSize is less than 1
     * @throws NullPointerException     if strategy is null
     */
    ConcurrentCache(int maxSize, EvictionStrategy<K> strategy) {
        if (maxSize < 1) {
            throw new IllegalArgumentException("maxSize must be at least 1, was: " + maxSize);
        }
        this.maxSize = maxSize;
        this.strategy = Objects.requireNonNull(strategy, "strategy must not be null");
        this.store = new HashMap<>(maxSize);
    }

    @Override
    public void put(K key, V value) {
        Objects.requireNonNull(key, "key must not be null");
        Objects.requireNonNull(value, "value must not be null");

        lock.writeLock().lock();
        try {
            boolean isUpdate = store.containsKey(key);
            if (!isUpdate) {
                evictIfNecessary();
            }
            store.put(key, value);
            strategy.onPut(key);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Optional<V> get(K key) {
        Objects.requireNonNull(key, "key must not be null");

        lock.readLock().lock();
        try {
            V value = store.get(key);
            if (value != null) {
                hitCount.incrementAndGet();
                strategy.onAccess(key);
                return Optional.of(value);
            }
            missCount.incrementAndGet();
            return Optional.empty();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void remove(K key) {
        Objects.requireNonNull(key, "key must not be null");

        lock.writeLock().lock();
        try {
            if (store.remove(key) != null) {
                strategy.onRemove(key);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void clear() {
        lock.writeLock().lock();
        try {
            store.clear();
            strategy.clear();
            hitCount.set(0);
            missCount.set(0);
            evictionCount.set(0);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public int size() {
        lock.readLock().lock();
        try {
            return store.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public CacheStatistics statistics() {
        return new CacheStatisticsSnapshot(
                hitCount.get(),
                missCount.get(),
                evictionCount.get());
    }

    /**
     * Evicts entries until the cache has room for one more. Must be called
     * while holding the write lock.
     */
    private void evictIfNecessary() {
        while (store.size() >= maxSize) {
            Optional<K> victim = strategy.evict();
            if (victim.isPresent()) {
                store.remove(victim.get());
                evictionCount.incrementAndGet();
            } else {
                // Strategy has no candidate -- should not happen if bookkeeping is correct,
                // but guard against infinite loop
                break;
            }
        }
    }
}
