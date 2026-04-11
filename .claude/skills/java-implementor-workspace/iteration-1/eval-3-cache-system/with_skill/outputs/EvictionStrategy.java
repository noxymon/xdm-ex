import java.util.Optional;

/**
 * Defines how a cache selects entries for eviction when capacity is exceeded.
 *
 * <p>Each strategy maintains its own internal bookkeeping (access order, frequency
 * counters, timestamps, etc.). The cache delegates eviction decisions to this
 * interface, enabling new eviction policies without modifying the cache itself.
 *
 * <p>Implementations are not required to be thread-safe; the owning cache is
 * responsible for synchronization.
 *
 * @param <K> the type of keys tracked by this strategy
 */
public interface EvictionStrategy<K> {

    /**
     * Records that the given key was accessed (read or written).
     *
     * @param key the key that was accessed, must not be null
     */
    void onAccess(K key);

    /**
     * Records that a new entry was added to the cache.
     *
     * @param key the key of the new entry, must not be null
     */
    void onPut(K key);

    /**
     * Records that an entry was removed from the cache (explicit removal, not eviction).
     *
     * @param key the key that was removed, must not be null
     */
    void onRemove(K key);

    /**
     * Selects and returns the key that should be evicted next according to this
     * strategy's policy. Returns empty if no candidate is available.
     *
     * @return the key to evict, or empty if there is nothing to evict
     */
    Optional<K> evict();

    /**
     * Removes all tracked state. Called when the cache is cleared.
     */
    void clear();
}
