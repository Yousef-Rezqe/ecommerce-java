package com.ecommerce.dao;
import com.ecommerce.model.Product;
import com.ecommerce.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public class ProductDAO {
    public List<Product> findAll() throws SQLException {
        String sql = "SELECT id, name, description, price, stock, image_url, created_by, created_at " +
                     "FROM products ORDER BY id DESC";
        List<Product> out = new ArrayList<>();
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(map(rs));
        }
        return out;
    }
    public Optional<Product> findById(long id) throws SQLException {
        String sql = "SELECT id, name, description, price, stock, image_url, created_by, created_at " +
                     "FROM products WHERE id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        }
    }
    public long create(Product p) throws SQLException {
        String sql = "INSERT INTO products (name, description, price, stock, image_url, created_by) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getName());
            ps.setString(2, p.getDescription());
            ps.setBigDecimal(3, p.getPrice());
            ps.setInt(4, p.getStock());
            ps.setString(5, p.getImageUrl());
            if (p.getCreatedBy() != null) ps.setLong(6, p.getCreatedBy());
            else ps.setNull(6, Types.BIGINT);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getLong(1);
            }
        }
        throw new SQLException("Failed to retrieve generated product id");
    }
    public boolean update(Product p) throws SQLException {
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, stock = ?, image_url = ? " +
                     "WHERE id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setString(2, p.getDescription());
            ps.setBigDecimal(3, p.getPrice());
            ps.setInt(4, p.getStock());
            ps.setString(5, p.getImageUrl());
            ps.setLong(6, p.getId());
            return ps.executeUpdate() > 0;
        }
    }
    public boolean delete(long id) throws SQLException {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }
    private Product map(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getLong("id"));
        p.setName(rs.getString("name"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setStock(rs.getInt("stock"));
        p.setImageUrl(rs.getString("image_url"));
        long cb = rs.getLong("created_by");
        p.setCreatedBy(rs.wasNull() ? null : cb);
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) p.setCreatedAt(ts.toInstant());
        return p;
    }
}
