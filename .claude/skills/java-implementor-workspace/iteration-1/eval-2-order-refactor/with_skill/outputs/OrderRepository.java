import java.math.BigDecimal;

/**
 * Abstraction for persisting orders.
 *
 * <p>Decouples the order processing logic from the specific persistence
 * mechanism (DIP). Implementations may use JDBC, JPA, or an in-memory
 * store for testing.
 */
public interface OrderRepository {

    /**
     * Saves an order for the given customer with the specified total.
     *
     * @param customerId the customer identifier
     * @param total      the order total after discounts
     */
    void save(String customerId, BigDecimal total);
}
