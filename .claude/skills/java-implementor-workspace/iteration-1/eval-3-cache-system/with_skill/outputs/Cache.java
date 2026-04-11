import java.util.Optional;

/**
 * A generic cache that maps keys to values with configurable eviction behavior.
 *
 * <p>Implementations must be thread-safe. Callers can retrieve cache statistics
 * via {@link #statistics()} without affecting cache behavior.
 *
 * @param <K> the type of keys maintained by this cache
 * @param <V> the type of mapped values
 */
public interface Cache<K, V> {

    /**
     * Associates the specified value with the specified key. If the cache previously
     * contained a mapping for the key, the old value is replaced.
     *
     * @param key   the key with which the value is associated, must not be null
     * @param value the value to associate with the key, must not be null
     * @throws NullPointerException if key or value is null
     */
    void put(K key, V value);

    /**
     * Returns the value associated with the specified key, or empty if no mapping exists
     * or the entry has been evicted.
     *
     * <p>A successful lookup counts as a cache hit; an absent key counts as a miss.
     *
     * @param key the key whose associated value is to be returned, must not be null
     * @return an {@code Optional} containing the value, or {@code Optional.empty()}
     * @throws NullPointerException if key is null
     */
    Optional<V> get(K key);

    /**
     * Removes the mapping for the specified key, if present.
     *
     * @param key the key whose mapping is to be removed, must not be null
     * @throws NullPointerException if key is null
     */
    void remove(K key);

    /**
     * Removes all entries from this cache and resets statistics.
     */
    void clear();

    /**
     * Returns the number of entries currently in the cache.
     *
     * @return the current size
     */
    int size();

    /**
     * Returns a snapshot of the current cache statistics.
     *
     * @return an immutable statistics snapshot
     */
    CacheStatistics statistics();
}
