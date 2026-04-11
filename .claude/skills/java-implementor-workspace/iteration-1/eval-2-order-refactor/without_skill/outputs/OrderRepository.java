public interface OrderRepository {

    /**
     * Persists the given order and returns the generated order ID.
     *
     * @param order the order to save
     * @return the generated order identifier
     */
    String save(Order order);
}
