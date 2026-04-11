import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Evicts entries that have exceeded a configured time-to-live (TTL).
 *
 * <p>Each key is associated with the timestamp of its most recent write.
 * On eviction, the oldest expired entry is removed. If no entries have expired,
 * the oldest entry overall is selected (the one closest to expiring).
 *
 * <p>A {@link Clock} is injected for testability, allowing deterministic
 * time control in tests.
 *
 * <p>This class is not thread-safe; the owning cache handles synchronization.
 *
 * @param <K> the type of keys tracked by this strategy
 */
public final class TtlEvictionStrategy<K> implements EvictionStrategy<K> {

    private final Duration ttl;
    private final Clock clock;
    // Insertion-ordered map: earliest entries appear first during iteration
    private final LinkedHashMap<K, Instant> insertionTimes = new LinkedHashMap<>();

    /**
     * Creates a TTL strategy with the specified duration and clock.
     *
     * @param ttl   the maximum time an entry may live before becoming eligible
     *              for eviction, must be positive
     * @param clock the clock used to determine the current time, must not be null
     * @throws NullPointerException     if ttl or clock is null
     * @throws IllegalArgumentException if ttl is zero or negative
     */
    public TtlEvictionStrategy(Duration ttl, Clock clock) {
        Objects.requireNonNull(ttl, "ttl must not be null");
        Objects.requireNonNull(clock, "clock must not be null");
        if (ttl.isZero() || ttl.isNegative()) {
            throw new IllegalArgumentException("ttl must be positive, was: " + ttl);
        }
        this.ttl = ttl;
        this.clock = clock;
    }

    /**
     * Creates a TTL strategy using the system UTC clock.
     *
     * @param ttl the maximum time an entry may live, must be positive
     * @throws NullPointerException     if ttl is null
     * @throws IllegalArgumentException if ttl is zero or negative
     */
    public TtlEvictionStrategy(Duration ttl) {
        this(ttl, Clock.systemUTC());
    }

    @Override
    public void onAccess(K key) {
        Objects.requireNonNull(key, "key must not be null");
        // TTL is based on insertion time, not access time -- no update needed
    }

    @Override
    public void onPut(K key) {
        Objects.requireNonNull(key, "key must not be null");
        // Refresh timestamp on put (re-insert moves to end of insertion order)
        insertionTimes.remove(key);
        insertionTimes.put(key, clock.instant());
    }

    @Override
    public void onRemove(K key) {
        Objects.requireNonNull(key, "key must not be null");
        insertionTimes.remove(key);
    }

    @Override
    public Optional<K> evict() {
        if (insertionTimes.isEmpty()) {
            return Optional.empty();
        }
        Instant now = clock.instant();

        // First, try to evict the oldest expired entry
        var iterator = insertionTimes.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<K, Instant> entry = iterator.next();
            if (Duration.between(entry.getValue(), now).compareTo(ttl) >= 0) {
                K key = entry.getKey();
                iterator.remove();
                return Optional.of(key);
            }
            // Entries are in insertion order, so if this one hasn't expired,
            // later ones may still be expired if they were re-put earlier.
            // But LinkedHashMap insertion order means the first is the oldest,
            // so if it hasn't expired, we fall through to evict it anyway
            // (it's the closest to expiring).
            break;
        }

        // No expired entries -- evict the oldest (closest to expiring)
        var oldest = insertionTimes.entrySet().iterator();
        if (oldest.hasNext()) {
            Map.Entry<K, Instant> entry = oldest.next();
            K key = entry.getKey();
            oldest.remove();
            return Optional.of(key);
        }

        return Optional.empty();
    }

    /**
     * Checks whether the entry for the given key has expired.
     *
     * @param key the key to check, must not be null
     * @return true if the key is tracked and has exceeded its TTL
     */
    public boolean isExpired(K key) {
        Objects.requireNonNull(key, "key must not be null");
        Instant insertedAt = insertionTimes.get(key);
        if (insertedAt == null) {
            return false;
        }
        return Duration.between(insertedAt, clock.instant()).compareTo(ttl) >= 0;
    }

    @Override
    public void clear() {
        insertionTimes.clear();
    }
}
