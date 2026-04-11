/**
 * Thrown when an order cannot be fulfilled because a product does not
 * have enough stock.
 *
 * <p>This is a checked exception because the caller can reasonably
 * recover (e.g. notify the user to adjust their cart).
 * Failure-capture information is included per Effective Java Item 75.
 */
public class InsufficientStockException extends Exception {

    private final String productId;
    private final int requestedQuantity;

    public InsufficientStockException(String productId, int requestedQuantity) {
        super("Insufficient stock for product '" + productId
                + "': requested " + requestedQuantity + " units");
        this.productId = productId;
        this.requestedQuantity = requestedQuantity;
    }

    public String productId() {
        return productId;
    }

    public int requestedQuantity() {
        return requestedQuantity;
    }
}
