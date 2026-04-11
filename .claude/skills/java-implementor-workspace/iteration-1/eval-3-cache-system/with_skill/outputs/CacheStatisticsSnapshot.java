import java.util.Objects;

/**
 * An immutable snapshot of cache statistics at a point in time.
 *
 * <p>This class is thread-safe because it is immutable (all fields are final
 * and primitive).
 */
public final class CacheStatisticsSnapshot implements CacheStatistics {

    private final long hitCount;
    private final long missCount;
    private final long evictionCount;

    /**
     * Creates a statistics snapshot with the given counters.
     *
     * @param hitCount      the number of cache hits, must be non-negative
     * @param missCount     the number of cache misses, must be non-negative
     * @param evictionCount the number of evictions, must be non-negative
     * @throws IllegalArgumentException if any count is negative
     */
    public CacheStatisticsSnapshot(long hitCount, long missCount, long evictionCount) {
        if (hitCount < 0) {
            throw new IllegalArgumentException("hitCount must be non-negative, was: " + hitCount);
        }
        if (missCount < 0) {
            throw new IllegalArgumentException("missCount must be non-negative, was: " + missCount);
        }
        if (evictionCount < 0) {
            throw new IllegalArgumentException("evictionCount must be non-negative, was: " + evictionCount);
        }
        this.hitCount = hitCount;
        this.missCount = missCount;
        this.evictionCount = evictionCount;
    }

    @Override
    public long hitCount() {
        return hitCount;
    }

    @Override
    public long missCount() {
        return missCount;
    }

    @Override
    public long evictionCount() {
        return evictionCount;
    }

    @Override
    public double hitRate() {
        long total = requestCount();
        return total == 0 ? 0.0 : (double) hitCount / total;
    }

    @Override
    public long requestCount() {
        return hitCount + missCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CacheStatisticsSnapshot that)) return false;
        return hitCount == that.hitCount
                && missCount == that.missCount
                && evictionCount == that.evictionCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hitCount, missCount, evictionCount);
    }

    @Override
    public String toString() {
        return String.format(
                "CacheStatistics[hits=%d, misses=%d, evictions=%d, hitRate=%.2f%%]",
                hitCount, missCount, evictionCount, hitRate() * 100);
    }
}
