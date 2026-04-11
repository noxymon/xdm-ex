import java.util.concurrent.atomic.AtomicLong;

/**
 * Thread-safe container for cache performance statistics.
 * Tracks hits, misses, and eviction counts using atomic counters
 * to support concurrent access without explicit synchronization.
 */
public class CacheStatistics {

    private final AtomicLong hits = new AtomicLong(0);
    private final AtomicLong misses = new AtomicLong(0);
    private final AtomicLong evictions = new AtomicLong(0);

    /** Records a cache hit. */
    public void recordHit() {
        hits.incrementAndGet();
    }

    /** Records a cache miss. */
    public void recordMiss() {
        misses.incrementAndGet();
    }

    /** Records an eviction event. */
    public void recordEviction() {
        evictions.incrementAndGet();
    }

    /** Returns the total number of cache hits. */
    public long getHits() {
        return hits.get();
    }

    /** Returns the total number of cache misses. */
    public long getMisses() {
        return misses.get();
    }

    /** Returns the total number of evictions. */
    public long getEvictions() {
        return evictions.get();
    }

    /** Returns the total number of requests (hits + misses). */
    public long getTotalRequests() {
        return hits.get() + misses.get();
    }

    /**
     * Returns the hit ratio as a value between 0.0 and 1.0.
     * Returns 0.0 if no requests have been made.
     */
    public double getHitRate() {
        long total = getTotalRequests();
        if (total == 0) {
            return 0.0;
        }
        return (double) hits.get() / total;
    }

    /**
     * Returns the miss ratio as a value between 0.0 and 1.0.
     * Returns 0.0 if no requests have been made.
     */
    public double getMissRate() {
        long total = getTotalRequests();
        if (total == 0) {
            return 0.0;
        }
        return (double) misses.get() / total;
    }

    /** Resets all statistics counters to zero. */
    public void reset() {
        hits.set(0);
        misses.set(0);
        evictions.set(0);
    }

    @Override
    public String toString() {
        return String.format(
                "CacheStatistics{hits=%d, misses=%d, evictions=%d, hitRate=%.2f%%}",
                hits.get(), misses.get(), evictions.get(), getHitRate() * 100);
    }
}
