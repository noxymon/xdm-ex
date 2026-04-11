/**
 * Defines the contract for cache eviction strategies.
 * Implementations determine which entry to evict when the cache is full
 * and track access patterns for eviction decisions.
 *
 * @param <K> the type of keys maintained by the cache
 */
public interface EvictionStrategy<K> {

    /**
     * Called when a key is accessed (get or put).
     * Implementations use this to update internal tracking structures.
     *
     * @param key the key that was accessed
     */
    void onAccess(K key);

    /**
     * Called when a new key is added to the cache.
     *
     * @param key the key that was added
     */
    void onPut(K key);

    /**
     * Called when a key is removed from the cache (either by eviction or explicit removal).
     *
     * @param key the key that was removed
     */
    void onRemove(K key);

    /**
     * Selects the key that should be evicted next according to this strategy.
     *
     * @return the key to evict, or null if there are no candidates
     */
    K evict();

    /**
     * Removes all tracked keys from this strategy, resetting its state.
     */
    void clear();
}
