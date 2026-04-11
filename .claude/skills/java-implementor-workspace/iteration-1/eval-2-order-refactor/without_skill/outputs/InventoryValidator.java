import java.util.List;

public class InventoryValidator {
    private final InventoryRepository inventoryRepository;

    public InventoryValidator(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    /**
     * Validates that all order items have sufficient stock.
     *
     * @param items the items to validate
     * @throws InsufficientStockException if any item lacks sufficient stock
     */
    public void validate(List<OrderItem> items) {
        for (OrderItem item : items) {
            int stock = inventoryRepository.getStock(item.getProductId());
            if (stock < item.getQuantity()) {
                throw new InsufficientStockException(
                        item.getProductId(), item.getQuantity(), stock);
            }
        }
    }
}
