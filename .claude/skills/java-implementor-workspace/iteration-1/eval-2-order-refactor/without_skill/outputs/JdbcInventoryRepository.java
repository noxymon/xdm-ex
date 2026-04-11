import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcInventoryRepository implements InventoryRepository {
    private final Connection connection;

    public JdbcInventoryRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int getStock(String productId) {
        String sql = "SELECT stock FROM inventory WHERE product_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("stock");
                }
                throw new InventoryException("Product not found: " + productId);
            }
        } catch (SQLException e) {
            throw new InventoryException("Failed to query stock for product: " + productId, e);
        }
    }
}
