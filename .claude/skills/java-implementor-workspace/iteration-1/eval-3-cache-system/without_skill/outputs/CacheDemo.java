import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Demonstrates usage of the caching system with different eviction strategies.
 */
public class CacheDemo {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== LRU Cache Demo ===");
        demonstrateLRU();

        System.out.println("\n=== LFU Cache Demo ===");
        demonstrateLFU();

        System.out.println("\n=== TTL Cache Demo ===");
        demonstrateTTL();

        System.out.println("\n=== Custom Strategy Demo ===");
        demonstrateCustomStrategy();
    }

    private static void demonstrateLRU() {
        CacheConfig<String> config = CacheConfig.<String>builder()
                .maxSize(3)
                .evictionType(CacheConfig.EvictionType.LRU)
                .build();

        Cache<String, Integer> cache = new Cache<>(config);

        cache.put("a", 1);
        cache.put("b", 2);
        cache.put("c", 3);

        // Access "a" so it becomes most recently used
        cache.get("a");

        // This should evict "b" (least recently used)
        cache.put("d", 4);

        System.out.println("After eviction (expect 'b' evicted):");
        System.out.println("  a present: " + cache.containsKey("a")); // true
        System.out.println("  b present: " + cache.containsKey("b")); // false
        System.out.println("  c present: " + cache.containsKey("c")); // true
        System.out.println("  d present: " + cache.containsKey("d")); // true
        System.out.println("  Stats: " + cache.getStatistics());
    }

    private static void demonstrateLFU() {
        CacheConfig<String> config = CacheConfig.<String>builder()
                .maxSize(3)
                .evictionType(CacheConfig.EvictionType.LFU)
                .build();

        Cache<String, Integer> cache = new Cache<>(config);

        cache.put("a", 1);
        cache.put("b", 2);
        cache.put("c", 3);

        // Access "a" and "c" multiple times
        cache.get("a");
        cache.get("a");
        cache.get("c");

        // This should evict "b" (least frequently used, accessed only on put)
        cache.put("d", 4);

        System.out.println("After eviction (expect 'b' evicted):");
        System.out.println("  a present: " + cache.containsKey("a")); // true
        System.out.println("  b present: " + cache.containsKey("b")); // false
        System.out.println("  c present: " + cache.containsKey("c")); // true
        System.out.println("  d present: " + cache.containsKey("d")); // true
        System.out.println("  Stats: " + cache.getStatistics());
    }

    private static void demonstrateTTL() throws InterruptedException {
        CacheConfig<String> config = CacheConfig.<String>builder()
                .maxSize(3)
                .ttlEviction(200, TimeUnit.MILLISECONDS)
                .build();

        Cache<String, Integer> cache = new Cache<>(config);

        cache.put("a", 1);
        cache.put("b", 2);

        System.out.println("Before TTL expiry:");
        System.out.println("  a present: " + cache.get("a").isPresent()); // true

        // Wait for entries to expire
        Thread.sleep(300);

        System.out.println("After TTL expiry:");
        System.out.println("  a present: " + cache.get("a").isPresent()); // false
        System.out.println("  Stats: " + cache.getStatistics());
    }

    private static void demonstrateCustomStrategy() {
        // You can supply any EvictionStrategy implementation
        EvictionStrategy<String> customLru = new LRUEvictionStrategy<>();

        CacheConfig<String> config = CacheConfig.<String>builder()
                .maxSize(2)
                .evictionStrategy(customLru)
                .build();

        Cache<String, String> cache = new Cache<>(config);

        cache.put("x", "hello");
        cache.put("y", "world");
        cache.put("z", "!"); // evicts "x"

        System.out.println("Custom strategy result:");
        System.out.println("  x present: " + cache.containsKey("x")); // false
        System.out.println("  y present: " + cache.containsKey("y")); // true
        System.out.println("  z present: " + cache.containsKey("z")); // true
        System.out.println("  Stats: " + cache.getStatistics());
    }
}
