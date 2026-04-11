import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * Processes customer orders by validating inventory, calculating totals
 * with discounts, persisting the order, and sending a confirmation.
 *
 * <p>This class coordinates the workflow but delegates each concern to
 * a focused collaborator (SRP). All dependencies are injected through
 * the constructor as abstractions (DIP), making this class easy to test
 * and extend.
 */
public final class OrderProcessor {

    private final InventoryService inventoryService;
    private final OrderRepository orderRepository;
    private final NotificationService notificationService;

    /**
     * Creates an {@code OrderProcessor} with the given collaborators.
     *
     * @param inventoryService   checks product availability
     * @param orderRepository    persists confirmed orders
     * @param notificationService sends order confirmations
     */
    public OrderProcessor(InventoryService inventoryService,
                          OrderRepository orderRepository,
                          NotificationService notificationService) {
        this.inventoryService = Objects.requireNonNull(inventoryService,
                "inventoryService must not be null");
        this.orderRepository = Objects.requireNonNull(orderRepository,
                "orderRepository must not be null");
        this.notificationService = Objects.requireNonNull(notificationService,
                "notificationService must not be null");
    }

    /**
     * Processes an order for the given customer.
     *
     * <p>The method validates that every item is in stock, computes the
     * subtotal, applies the customer-type discount, saves the order, and
     * sends a confirmation notification.
     *
     * @param customerId   the customer placing the order
     * @param customerType the customer's tier (determines discount)
     * @param items        the items to order (must not be empty)
     * @return the final order total after discount
     * @throws IllegalArgumentException    if items is empty or customerId is blank
     * @throws InsufficientStockException  if any item lacks sufficient inventory
     */
    public BigDecimal processOrder(String customerId,
                                   CustomerType customerType,
                                   List<OrderItem> items) throws InsufficientStockException {
        validateInputs(customerId, customerType, items);
        validateInventory(items);

        BigDecimal subtotal = calculateSubtotal(items);
        BigDecimal total = DiscountCalculator.applyDiscount(subtotal, customerType);

        orderRepository.save(customerId, total);
        notificationService.sendOrderConfirmation(customerId, total);

        return total;
    }

    private void validateInputs(String customerId,
                                CustomerType customerType,
                                List<OrderItem> items) {
        Objects.requireNonNull(customerId, "customerId must not be null");
        Objects.requireNonNull(customerType, "customerType must not be null");
        Objects.requireNonNull(items, "items must not be null");
        if (customerId.isBlank()) {
            throw new IllegalArgumentException("customerId must not be blank");
        }
        if (items.isEmpty()) {
            throw new IllegalArgumentException("items must not be empty");
        }
    }

    private void validateInventory(List<OrderItem> items) throws InsufficientStockException {
        for (OrderItem item : items) {
            if (!inventoryService.isAvailable(item.productId(), item.quantity())) {
                throw new InsufficientStockException(item.productId(), item.quantity());
            }
        }
    }

    private BigDecimal calculateSubtotal(List<OrderItem> items) {
        return items.stream()
                .map(OrderItem::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
