/**
 * An immutable snapshot of cache performance counters.
 *
 * <p>Instances are obtained via {@link Cache#statistics()} and reflect the
 * state at the moment the snapshot was taken.
 */
public interface CacheStatistics {

    /**
     * Returns the total number of cache hits (successful {@code get} calls).
     *
     * @return the hit count, always non-negative
     */
    long hitCount();

    /**
     * Returns the total number of cache misses (unsuccessful {@code get} calls).
     *
     * @return the miss count, always non-negative
     */
    long missCount();

    /**
     * Returns the total number of entries evicted by the eviction strategy.
     *
     * @return the eviction count, always non-negative
     */
    long evictionCount();

    /**
     * Returns the hit rate as a ratio between 0.0 and 1.0.
     * Returns 0.0 if no requests have been made.
     *
     * @return the hit rate
     */
    double hitRate();

    /**
     * Returns the total number of requests ({@code hitCount + missCount}).
     *
     * @return the total request count, always non-negative
     */
    long requestCount();
}
