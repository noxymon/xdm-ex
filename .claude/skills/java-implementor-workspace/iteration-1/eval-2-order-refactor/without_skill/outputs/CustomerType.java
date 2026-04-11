public enum CustomerType {
    REGULAR(0.0),
    PREMIUM(0.10),
    VIP(0.20);

    private final double discountRate;

    CustomerType(double discountRate) {
        this.discountRate = discountRate;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public double applyDiscount(double total) {
        return total * (1.0 - discountRate);
    }
}
