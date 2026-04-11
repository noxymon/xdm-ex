public interface InventoryRepository {

    /**
     * Returns the available stock for the given product.
     *
     * @param productId the product identifier
     * @return the current stock level
     * @throws InventoryException if the product is not found or a data access error occurs
     */
    int getStock(String productId);
}
