import java.util.List;

public class OrderProcessor {
    private final InventoryValidator inventoryValidator;
    private final PricingService pricingService;
    private final OrderRepository orderRepository;
    private final OrderConfirmationNotifier confirmationNotifier;

    public OrderProcessor(InventoryValidator inventoryValidator,
                          PricingService pricingService,
                          OrderRepository orderRepository,
                          OrderConfirmationNotifier confirmationNotifier) {
        this.inventoryValidator = inventoryValidator;
        this.pricingService = pricingService;
        this.orderRepository = orderRepository;
        this.confirmationNotifier = confirmationNotifier;
    }

    /**
     * Processes an order: validates inventory, calculates the discounted total,
     * persists the order, and sends a confirmation email.
     *
     * @param customerId   the customer placing the order
     * @param customerType the customer's type (determines discount)
     * @param items        the items to order
     * @return the created Order
     * @throws IllegalArgumentException    if items is null or empty
     * @throws InsufficientStockException  if any item lacks sufficient stock
     */
    public Order processOrder(String customerId, CustomerType customerType, List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }

        inventoryValidator.validate(items);

        double total = pricingService.calculateTotal(items, customerType);

        Order order = new Order(customerId, customerType, items, total);
        orderRepository.save(order);

        confirmationNotifier.notify(order);

        return order;
    }
}
