import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;

/**
 * Evicts the least-recently-used entry when the cache reaches capacity.
 *
 * <p>Maintains access order using a {@link LinkedHashSet} that is re-ordered
 * on every access. The first element in iteration order is always the least
 * recently used.
 *
 * <p>This class is not thread-safe; the owning cache handles synchronization.
 *
 * @param <K> the type of keys tracked by this strategy
 */
public final class LruEvictionStrategy<K> implements EvictionStrategy<K> {

    private final LinkedHashSet<K> accessOrder = new LinkedHashSet<>();

    @Override
    public void onAccess(K key) {
        Objects.requireNonNull(key, "key must not be null");
        // Move to end (most recently used) by removing and re-adding
        accessOrder.remove(key);
        accessOrder.add(key);
    }

    @Override
    public void onPut(K key) {
        Objects.requireNonNull(key, "key must not be null");
        accessOrder.remove(key);
        accessOrder.add(key);
    }

    @Override
    public void onRemove(K key) {
        Objects.requireNonNull(key, "key must not be null");
        accessOrder.remove(key);
    }

    @Override
    public Optional<K> evict() {
        var iterator = accessOrder.iterator();
        if (!iterator.hasNext()) {
            return Optional.empty();
        }
        K oldest = iterator.next();
        iterator.remove();
        return Optional.of(oldest);
    }

    @Override
    public void clear() {
        accessOrder.clear();
    }
}
