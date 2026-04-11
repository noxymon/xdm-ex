import java.util.List;

public class PricingService {

    /**
     * Calculates the total price for the given items after applying the
     * customer-type discount.
     *
     * @param items        the order items
     * @param customerType the customer type (determines discount)
     * @return the discounted total
     */
    public double calculateTotal(List<OrderItem> items, CustomerType customerType) {
        double subtotal = items.stream()
                .mapToDouble(OrderItem::getLineTotal)
                .sum();
        return customerType.applyDiscount(subtotal);
    }
}
