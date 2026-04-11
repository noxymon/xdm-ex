import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Time-To-Live (TTL) eviction strategy.
 * Each entry has an expiration time. When eviction is needed, the oldest expired
 * entry is evicted first. If no entries have expired, the entry closest to
 * expiration (i.e., the one inserted earliest) is evicted.
 * <p>
 * Uses a {@link LinkedHashMap} ordered by insertion time to efficiently find
 * the oldest/most-expired entry.
 *
 * @param <K> the type of keys maintained by the cache
 */
public class TTLEvictionStrategy<K> implements EvictionStrategy<K> {

    private final long ttlMillis;

    /** Maps keys to their expiration timestamps in milliseconds, ordered by insertion. */
    private final LinkedHashMap<K, Long> expirationMap = new LinkedHashMap<>();

    /**
     * Creates a TTL eviction strategy with the specified time-to-live duration.
     *
     * @param duration the TTL duration
     * @param unit     the time unit for the duration
     */
    public TTLEvictionStrategy(long duration, TimeUnit unit) {
        this.ttlMillis = unit.toMillis(duration);
    }

    @Override
    public void onAccess(K key) {
        // TTL is based on creation time, not access time — no refresh on access.
        // If you want sliding TTL, uncomment the lines below:
        // expirationMap.remove(key);
        // expirationMap.put(key, System.currentTimeMillis() + ttlMillis);
    }

    @Override
    public void onPut(K key) {
        // Remove first to reset position in insertion order
        expirationMap.remove(key);
        expirationMap.put(key, System.currentTimeMillis() + ttlMillis);
    }

    @Override
    public void onRemove(K key) {
        expirationMap.remove(key);
    }

    @Override
    public K evict() {
        if (expirationMap.isEmpty()) {
            return null;
        }
        long now = System.currentTimeMillis();

        // First, try to find an expired entry (iterate in insertion order — oldest first)
        Iterator<Map.Entry<K, Long>> it = expirationMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<K, Long> entry = it.next();
            if (entry.getValue() <= now) {
                K key = entry.getKey();
                it.remove();
                return key;
            }
        }

        // No expired entries; evict the oldest (first in insertion order)
        K oldest = expirationMap.keySet().iterator().next();
        expirationMap.remove(oldest);
        return oldest;
    }

    /**
     * Checks whether the given key has expired based on its TTL.
     *
     * @param key the key to check
     * @return true if the key has expired, false if it is still valid or not tracked
     */
    public boolean isExpired(K key) {
        Long expiration = expirationMap.get(key);
        if (expiration == null) {
            return false;
        }
        return System.currentTimeMillis() >= expiration;
    }

    /**
     * Removes all expired keys from the strategy and returns them.
     * The caller is responsible for also removing these keys from the cache store.
     *
     * @return a map of expired keys (empty if none are expired)
     */
    public Map<K, Long> removeExpired() {
        Map<K, Long> expired = new HashMap<>();
        long now = System.currentTimeMillis();
        Iterator<Map.Entry<K, Long>> it = expirationMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<K, Long> entry = it.next();
            if (entry.getValue() <= now) {
                expired.put(entry.getKey(), entry.getValue());
                it.remove();
            }
        }
        return expired;
    }

    @Override
    public void clear() {
        expirationMap.clear();
    }
}
