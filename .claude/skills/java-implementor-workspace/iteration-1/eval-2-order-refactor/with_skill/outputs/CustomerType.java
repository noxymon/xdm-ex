/**
 * Represents the type of customer, each with an associated discount rate.
 *
 * <p>Adding a new customer tier requires only adding a new enum constant
 * with its discount rate -- no existing code needs modification (OCP).
 */
public enum CustomerType {

    REGULAR(0.00),
    PREMIUM(0.10),
    VIP(0.20);

    private final double discountRate;

    CustomerType(double discountRate) {
        this.discountRate = discountRate;
    }

    /**
     * Returns the discount rate as a fraction (e.g. 0.10 for 10%).
     */
    public double discountRate() {
        return discountRate;
    }
}
