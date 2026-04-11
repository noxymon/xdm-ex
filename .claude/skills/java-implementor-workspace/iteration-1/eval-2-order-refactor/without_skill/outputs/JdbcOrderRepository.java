import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcOrderRepository implements OrderRepository {
    private final Connection connection;

    public JdbcOrderRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public String save(Order order) {
        String sql = "INSERT INTO orders (customer_id, total) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, order.getCustomerId());
            ps.setDouble(2, order.getTotal());
            ps.executeUpdate();
            return order.getCustomerId(); // Simplified; a real implementation would return a generated ID
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save order for customer: " + order.getCustomerId(), e);
        }
    }
}
