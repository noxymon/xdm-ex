import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.TreeMap;

/**
 * Least Frequently Used (LFU) eviction strategy.
 * Evicts the key with the lowest access frequency. When multiple keys share
 * the same lowest frequency, the least recently added among them is evicted.
 * Uses a frequency map and a tree map of frequency buckets for O(1) eviction lookup.
 *
 * @param <K> the type of keys maintained by the cache
 */
public class LFUEvictionStrategy<K> implements EvictionStrategy<K> {

    /** Maps each key to its current access frequency. */
    private final Map<K, Integer> frequencyMap = new HashMap<>();

    /**
     * Maps each frequency count to the set of keys with that frequency.
     * LinkedHashSet preserves insertion order so ties are broken by age (oldest first).
     * TreeMap keeps frequencies sorted so we can quickly find the minimum.
     */
    private final TreeMap<Integer, LinkedHashSet<K>> frequencyBuckets = new TreeMap<>();

    @Override
    public void onAccess(K key) {
        if (!frequencyMap.containsKey(key)) {
            return;
        }
        incrementFrequency(key);
    }

    @Override
    public void onPut(K key) {
        if (frequencyMap.containsKey(key)) {
            // Existing key updated, treat as access
            incrementFrequency(key);
        } else {
            // New key starts at frequency 1
            frequencyMap.put(key, 1);
            frequencyBuckets.computeIfAbsent(1, f -> new LinkedHashSet<>()).add(key);
        }
    }

    @Override
    public void onRemove(K key) {
        Integer freq = frequencyMap.remove(key);
        if (freq != null) {
            LinkedHashSet<K> bucket = frequencyBuckets.get(freq);
            if (bucket != null) {
                bucket.remove(key);
                if (bucket.isEmpty()) {
                    frequencyBuckets.remove(freq);
                }
            }
        }
    }

    @Override
    public K evict() {
        if (frequencyBuckets.isEmpty()) {
            return null;
        }
        // Get the bucket with the lowest frequency
        Map.Entry<Integer, LinkedHashSet<K>> lowestEntry = frequencyBuckets.firstEntry();
        LinkedHashSet<K> bucket = lowestEntry.getValue();
        // Pick the first (oldest) key in that bucket
        K victim = bucket.iterator().next();
        bucket.remove(victim);
        if (bucket.isEmpty()) {
            frequencyBuckets.remove(lowestEntry.getKey());
        }
        frequencyMap.remove(victim);
        return victim;
    }

    @Override
    public void clear() {
        frequencyMap.clear();
        frequencyBuckets.clear();
    }

    private void incrementFrequency(K key) {
        int oldFreq = frequencyMap.get(key);
        int newFreq = oldFreq + 1;
        frequencyMap.put(key, newFreq);

        // Remove from old bucket
        LinkedHashSet<K> oldBucket = frequencyBuckets.get(oldFreq);
        if (oldBucket != null) {
            oldBucket.remove(key);
            if (oldBucket.isEmpty()) {
                frequencyBuckets.remove(oldFreq);
            }
        }

        // Add to new bucket
        frequencyBuckets.computeIfAbsent(newFreq, f -> new LinkedHashSet<>()).add(key);
    }
}
