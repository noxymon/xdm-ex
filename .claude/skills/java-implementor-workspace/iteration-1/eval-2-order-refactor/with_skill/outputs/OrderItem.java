import java.math.BigDecimal;
import java.util.Objects;

/**
 * An immutable representation of a single line item in an order.
 *
 * <p>Uses {@link BigDecimal} for the price to avoid floating-point
 * rounding issues in monetary calculations (Effective Java Item 60).
 */
public final class OrderItem {

    private final String productId;
    private final int quantity;
    private final BigDecimal unitPrice;

    public OrderItem(String productId, int quantity, BigDecimal unitPrice) {
        this.productId = Objects.requireNonNull(productId, "productId must not be null");
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive, got: " + quantity);
        }
        this.unitPrice = Objects.requireNonNull(unitPrice, "unitPrice must not be null");
        if (unitPrice.signum() < 0) {
            throw new IllegalArgumentException("unitPrice must not be negative, got: " + unitPrice);
        }
    }

    public String productId() {
        return productId;
    }

    public int quantity() {
        return quantity;
    }

    public BigDecimal unitPrice() {
        return unitPrice;
    }

    /**
     * Returns the line total (unitPrice * quantity).
     */
    public BigDecimal lineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    @Override
    public String toString() {
        return "OrderItem[productId=" + productId
                + ", quantity=" + quantity
                + ", unitPrice=" + unitPrice + "]";
    }
}
