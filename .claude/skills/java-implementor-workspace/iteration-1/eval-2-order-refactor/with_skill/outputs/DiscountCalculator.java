import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Calculates the discounted total for an order based on customer type.
 *
 * <p>Discount rates are defined on the {@link CustomerType} enum, so adding
 * a new customer tier does not require modifying this class (OCP).
 */
public final class DiscountCalculator {

    private DiscountCalculator() {
        throw new AssertionError("Utility class -- do not instantiate");
    }

    /**
     * Applies the discount associated with the given customer type to the subtotal.
     *
     * @param subtotal     the pre-discount order total (must be non-negative)
     * @param customerType the type of customer
     * @return the discounted total, rounded to 2 decimal places using HALF_UP
     * @throws NullPointerException     if either argument is null
     * @throws IllegalArgumentException if subtotal is negative
     */
    public static BigDecimal applyDiscount(BigDecimal subtotal, CustomerType customerType) {
        Objects.requireNonNull(subtotal, "subtotal must not be null");
        Objects.requireNonNull(customerType, "customerType must not be null");
        if (subtotal.signum() < 0) {
            throw new IllegalArgumentException("subtotal must not be negative, got: " + subtotal);
        }

        BigDecimal discountMultiplier = BigDecimal.ONE.subtract(
                BigDecimal.valueOf(customerType.discountRate()));
        return subtotal.multiply(discountMultiplier)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
