import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Immutable configuration for a {@link Cache} instance.
 * Built using the fluent {@link Builder} pattern.
 *
 * @param <K> the type of keys maintained by the cache
 */
public class CacheConfig<K> {

    /**
     * Enum of built-in eviction strategy types for convenient configuration.
     */
    public enum EvictionType {
        LRU,
        LFU,
        TTL
    }

    private final int maxSize;
    private final EvictionStrategy<K> evictionStrategy;

    private CacheConfig(Builder<K> builder) {
        this.maxSize = builder.maxSize;
        this.evictionStrategy = builder.evictionStrategy;
    }

    /** Returns the maximum number of entries the cache can hold. */
    public int getMaxSize() {
        return maxSize;
    }

    /** Returns the eviction strategy instance to use. */
    public EvictionStrategy<K> getEvictionStrategy() {
        return evictionStrategy;
    }

    /**
     * Creates a new builder for constructing a {@link CacheConfig}.
     *
     * @param <K> the key type
     * @return a new builder
     */
    public static <K> Builder<K> builder() {
        return new Builder<>();
    }

    /**
     * Fluent builder for {@link CacheConfig}.
     *
     * @param <K> the key type
     */
    public static class Builder<K> {
        private int maxSize = 100;
        private EvictionStrategy<K> evictionStrategy;

        private Builder() {
        }

        /**
         * Sets the maximum number of entries the cache may hold.
         *
         * @param maxSize the maximum size (must be positive)
         * @return this builder
         */
        public Builder<K> maxSize(int maxSize) {
            if (maxSize <= 0) {
                throw new IllegalArgumentException("maxSize must be positive, got: " + maxSize);
            }
            this.maxSize = maxSize;
            return this;
        }

        /**
         * Sets a custom eviction strategy instance.
         *
         * @param strategy the eviction strategy to use
         * @return this builder
         */
        public Builder<K> evictionStrategy(EvictionStrategy<K> strategy) {
            this.evictionStrategy = Objects.requireNonNull(strategy, "strategy must not be null");
            return this;
        }

        /**
         * Convenience method to select a built-in eviction strategy by type.
         * For TTL, use {@link #ttlEviction(long, TimeUnit)} instead.
         *
         * @param type the eviction type (LRU or LFU)
         * @return this builder
         */
        @SuppressWarnings("unchecked")
        public Builder<K> evictionType(EvictionType type) {
            switch (type) {
                case LRU:
                    this.evictionStrategy = new LRUEvictionStrategy<>();
                    break;
                case LFU:
                    this.evictionStrategy = new LFUEvictionStrategy<>();
                    break;
                case TTL:
                    throw new IllegalArgumentException(
                            "For TTL eviction, use ttlEviction(duration, unit) to specify the TTL duration");
                default:
                    throw new IllegalArgumentException("Unknown eviction type: " + type);
            }
            return this;
        }

        /**
         * Configures TTL-based eviction with the specified duration.
         *
         * @param duration the time-to-live duration
         * @param unit     the time unit
         * @return this builder
         */
        public Builder<K> ttlEviction(long duration, TimeUnit unit) {
            this.evictionStrategy = new TTLEvictionStrategy<>(duration, unit);
            return this;
        }

        /**
         * Builds the configuration, applying defaults for any unset fields.
         * Defaults to LRU eviction if no strategy was specified.
         *
         * @return an immutable {@link CacheConfig}
         */
        public CacheConfig<K> build() {
            if (evictionStrategy == null) {
                evictionStrategy = new LRUEvictionStrategy<>();
            }
            return new CacheConfig<>(this);
        }
    }
}
