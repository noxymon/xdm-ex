import java.time.Clock;
import java.time.Duration;
import java.util.Objects;

/**
 * A builder for constructing {@link Cache} instances with a fluent API.
 *
 * <p>At minimum, a maximum size and an eviction strategy must be specified.
 * Convenience factory methods are provided for the built-in strategies
 * (LRU, LFU, TTL).
 *
 * <h3>Usage examples</h3>
 * <pre>{@code
 * // LRU cache with max 1000 entries
 * Cache<String, User> cache = CacheBuilder.<String, User>newBuilder()
 *         .maxSize(1000)
 *         .lru()
 *         .build();
 *
 * // LFU cache
 * Cache<String, byte[]> cache = CacheBuilder.<String, byte[]>newBuilder()
 *         .maxSize(500)
 *         .lfu()
 *         .build();
 *
 * // TTL cache with 5-minute expiry
 * Cache<String, Session> cache = CacheBuilder.<String, Session>newBuilder()
 *         .maxSize(10_000)
 *         .ttl(Duration.ofMinutes(5))
 *         .build();
 *
 * // Custom eviction strategy
 * Cache<String, Data> cache = CacheBuilder.<String, Data>newBuilder()
 *         .maxSize(200)
 *         .evictionStrategy(new MyCustomStrategy<>())
 *         .build();
 * }</pre>
 *
 * @param <K> the type of keys for the cache being built
 * @param <V> the type of values for the cache being built
 */
public final class CacheBuilder<K, V> {

    private int maxSize = -1;
    private EvictionStrategy<K> strategy;

    private CacheBuilder() {
        // Use the static factory method
    }

    /**
     * Creates a new builder instance.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @return a new builder
     */
    public static <K, V> CacheBuilder<K, V> newBuilder() {
        return new CacheBuilder<>();
    }

    /**
     * Sets the maximum number of entries the cache will hold before evicting.
     *
     * @param maxSize the maximum size, must be at least 1
     * @return this builder
     * @throws IllegalArgumentException if maxSize is less than 1
     */
    public CacheBuilder<K, V> maxSize(int maxSize) {
        if (maxSize < 1) {
            throw new IllegalArgumentException("maxSize must be at least 1, was: " + maxSize);
        }
        this.maxSize = maxSize;
        return this;
    }

    /**
     * Sets a custom eviction strategy.
     *
     * @param strategy the eviction strategy, must not be null
     * @return this builder
     * @throws NullPointerException if strategy is null
     */
    public CacheBuilder<K, V> evictionStrategy(EvictionStrategy<K> strategy) {
        this.strategy = Objects.requireNonNull(strategy, "strategy must not be null");
        return this;
    }

    /**
     * Configures the cache to use least-recently-used (LRU) eviction.
     *
     * @return this builder
     */
    public CacheBuilder<K, V> lru() {
        this.strategy = new LruEvictionStrategy<>();
        return this;
    }

    /**
     * Configures the cache to use least-frequently-used (LFU) eviction.
     *
     * @return this builder
     */
    public CacheBuilder<K, V> lfu() {
        this.strategy = new LfuEvictionStrategy<>();
        return this;
    }

    /**
     * Configures the cache to use time-to-live (TTL) eviction with the
     * system UTC clock.
     *
     * @param ttl the time-to-live duration, must be positive
     * @return this builder
     * @throws NullPointerException     if ttl is null
     * @throws IllegalArgumentException if ttl is zero or negative
     */
    public CacheBuilder<K, V> ttl(Duration ttl) {
        this.strategy = new TtlEvictionStrategy<>(ttl);
        return this;
    }

    /**
     * Configures the cache to use time-to-live (TTL) eviction with a
     * custom clock (useful for testing).
     *
     * @param ttl   the time-to-live duration, must be positive
     * @param clock the clock to use for time calculations, must not be null
     * @return this builder
     * @throws NullPointerException     if ttl or clock is null
     * @throws IllegalArgumentException if ttl is zero or negative
     */
    public CacheBuilder<K, V> ttl(Duration ttl, Clock clock) {
        this.strategy = new TtlEvictionStrategy<>(ttl, clock);
        return this;
    }

    /**
     * Builds and returns a new thread-safe {@link Cache} instance.
     *
     * @return a configured cache
     * @throws IllegalStateException if maxSize or eviction strategy has not been set
     */
    public Cache<K, V> build() {
        if (maxSize < 1) {
            throw new IllegalStateException(
                    "maxSize must be configured before building; call maxSize(int)");
        }
        if (strategy == null) {
            throw new IllegalStateException(
                    "eviction strategy must be configured before building; "
                            + "call lru(), lfu(), ttl(Duration), or evictionStrategy(EvictionStrategy)");
        }
        return new ConcurrentCache<>(maxSize, strategy);
    }
}
