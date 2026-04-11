import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Evicts the least-frequently-used entry when the cache reaches capacity.
 *
 * <p>Tracks access counts per key. When multiple keys share the lowest frequency,
 * the one that reached that frequency earliest is evicted (insertion order as
 * tie-breaker via iteration order of the frequency map).
 *
 * <p>This class is not thread-safe; the owning cache handles synchronization.
 *
 * @param <K> the type of keys tracked by this strategy
 */
public final class LfuEvictionStrategy<K> implements EvictionStrategy<K> {

    private final Map<K, Long> frequencyMap = new HashMap<>();

    @Override
    public void onAccess(K key) {
        Objects.requireNonNull(key, "key must not be null");
        frequencyMap.computeIfPresent(key, (k, count) -> count + 1);
    }

    @Override
    public void onPut(K key) {
        Objects.requireNonNull(key, "key must not be null");
        frequencyMap.merge(key, 1L, Long::sum);
    }

    @Override
    public void onRemove(K key) {
        Objects.requireNonNull(key, "key must not be null");
        frequencyMap.remove(key);
    }

    @Override
    public Optional<K> evict() {
        if (frequencyMap.isEmpty()) {
            return Optional.empty();
        }
        // Find the key with the minimum frequency
        K minKey = null;
        long minFreq = Long.MAX_VALUE;
        for (Map.Entry<K, Long> entry : frequencyMap.entrySet()) {
            if (entry.getValue() < minFreq) {
                minFreq = entry.getValue();
                minKey = entry.getKey();
            }
        }
        if (minKey != null) {
            frequencyMap.remove(minKey);
        }
        return Optional.ofNullable(minKey);
    }

    @Override
    public void clear() {
        frequencyMap.clear();
    }
}
