public interface CustomerRepository {

    /**
     * Returns the email address for the given customer.
     *
     * @param customerId the customer identifier
     * @return the customer's email address
     */
    String getEmail(String customerId);
}
