import java.math.BigDecimal;

/**
 * Abstraction for sending order-related notifications to customers.
 *
 * <p>Separating notification from order processing satisfies SRP and
 * allows swapping implementations (email, SMS, push) without touching
 * the core order logic.
 */
public interface NotificationService {

    /**
     * Sends an order confirmation to the specified customer.
     *
     * @param customerId the customer identifier
     * @param orderTotal the confirmed order total
     */
    void sendOrderConfirmation(String customerId, BigDecimal orderTotal);
}
