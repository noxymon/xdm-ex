import java.util.List;

public class Order {
    private final String customerId;
    private final CustomerType customerType;
    private final List<OrderItem> items;
    private final double total;

    public Order(String customerId, CustomerType customerType, List<OrderItem> items, double total) {
        this.customerId = customerId;
        this.customerType = customerType;
        this.items = List.copyOf(items);
        this.total = total;
    }

    public String getCustomerId() {
        return customerId;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public double getTotal() {
        return total;
    }
}
