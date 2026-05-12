package com.ecommerce.dao;
import com.ecommerce.model.Order;
import com.ecommerce.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class OrderDAO {
    public long create(Order o) throws SQLException {
        String sql = "INSERT INTO orders " +
                "(user_id, product_id, product_name, quantity, unit_price, total_price, " +
                " full_name, phone, address, city, notes, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, o.getUserId());
            ps.setLong(2, o.getProductId());
            ps.setString(3, o.getProductName());
            ps.setInt(4, o.getQuantity());
            ps.setBigDecimal(5, o.getUnitPrice());
            ps.setBigDecimal(6, o.getTotalPrice());
            ps.setString(7, o.getFullName());
            ps.setString(8, o.getPhone());
            ps.setString(9, o.getAddress());
            ps.setString(10, o.getCity());
            ps.setString(11, o.getNotes());
            ps.setString(12, o.getStatus() == null ? "PENDING" : o.getStatus());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getLong(1);
            }
        }
        throw new SQLException("Failed to retrieve generated order id");
    }
    public List<Order> findByUser(long userId) throws SQLException {
        String sql = "SELECT id, user_id, product_id, product_name, quantity, unit_price, " +
                "total_price, full_name, phone, address, city, notes, status, created_at " +
                "FROM orders WHERE user_id = ? ORDER BY created_at DESC";
        List<Order> out = new ArrayList<>();
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(map(rs));
            }
        }
        return out;
    }
    public List<Order> findAll() throws SQLException {
        String sql = "SELECT id, user_id, product_id, product_name, quantity, unit_price, " +
                "total_price, full_name, phone, address, city, notes, status, created_at " +
                "FROM orders ORDER BY created_at DESC";
        List<Order> out = new ArrayList<>();
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(map(rs));
        }
        return out;
    }
    public boolean updateStatus(long id, String status) throws SQLException {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setLong(2, id);
            return ps.executeUpdate() > 0;
        }
    }
    private Order map(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setId(rs.getLong("id"));
        o.setUserId(rs.getLong("user_id"));
        o.setProductId(rs.getLong("product_id"));
        o.setProductName(rs.getString("product_name"));
        o.setQuantity(rs.getInt("quantity"));
        o.setUnitPrice(rs.getBigDecimal("unit_price"));
        o.setTotalPrice(rs.getBigDecimal("total_price"));
        o.setFullName(rs.getString("full_name"));
        o.setPhone(rs.getString("phone"));
        o.setAddress(rs.getString("address"));
        o.setCity(rs.getString("city"));
        o.setNotes(rs.getString("notes"));
        o.setStatus(rs.getString("status"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) o.setCreatedAt(ts.toInstant());
        return o;
    }
}
