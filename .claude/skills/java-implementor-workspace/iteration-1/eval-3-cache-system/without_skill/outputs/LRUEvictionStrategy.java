import java.util.LinkedHashSet;

/**
 * Least Recently Used (LRU) eviction strategy.
 * Evicts the key that has not been accessed for the longest time.
 * Uses a {@link LinkedHashSet} to maintain insertion/access order.
 *
 * @param <K> the type of keys maintained by the cache
 */
public class LRUEvictionStrategy<K> implements EvictionStrategy<K> {

    private final LinkedHashSet<K> accessOrder = new LinkedHashSet<>();

    @Override
    public void onAccess(K key) {
        // Move to end (most recently used) by removing and re-adding
        accessOrder.remove(key);
        accessOrder.add(key);
    }

    @Override
    public void onPut(K key) {
        accessOrder.remove(key);
        accessOrder.add(key);
    }

    @Override
    public void onRemove(K key) {
        accessOrder.remove(key);
    }

    @Override
    public K evict() {
        if (accessOrder.isEmpty()) {
            return null;
        }
        // The first element is the least recently used
        K oldest = accessOrder.iterator().next();
        accessOrder.remove(oldest);
        return oldest;
    }

    @Override
    public void clear() {
        accessOrder.clear();
    }
}
