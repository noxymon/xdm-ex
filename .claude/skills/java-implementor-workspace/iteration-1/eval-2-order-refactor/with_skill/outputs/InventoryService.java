/**
 * Abstraction for checking product inventory availability.
 *
 * <p>High-level order processing code depends on this interface rather than
 * on a concrete database implementation (DIP).
 */
public interface InventoryService {

    /**
     * Returns {@code true} if the given product has at least {@code requiredQuantity}
     * units in stock.
     *
     * @param productId        the product identifier
     * @param requiredQuantity the number of units required
     * @return {@code true} if sufficient stock is available
     * @throws IllegalArgumentException if {@code requiredQuantity} is not positive
     */
    boolean isAvailable(String productId, int requiredQuantity);
}
