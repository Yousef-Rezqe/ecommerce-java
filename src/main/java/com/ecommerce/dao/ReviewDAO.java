package com.ecommerce.dao;
import com.ecommerce.model.Review;
import com.ecommerce.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class ReviewDAO {
    public static class RatingSummary {
        private final double average;
        private final int count;
        public RatingSummary(double average, int count) {
            this.average = average;
            this.count = count;
        }
        public double getAverage() { return average; }
        public int getCount() { return count; }
    }
    private static final String JOIN_SELECT =
            "SELECT r.id, r.product_id, r.user_id, u.username, r.rating, r.comment, r.created_at " +
            "FROM reviews r JOIN users u ON u.id = r.user_id ";
    public List<Review> findRecent(int limit) throws SQLException {
        String sql = JOIN_SELECT + "ORDER BY r.created_at DESC LIMIT ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                List<Review> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        }
    }
    public List<Review> findByUser(long userId) throws SQLException {
        String sql = JOIN_SELECT + "WHERE r.user_id = ? ORDER BY r.created_at DESC";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Review> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        }
    }
    public List<Review> findByProduct(long productId) throws SQLException {
        String sql = JOIN_SELECT + "WHERE r.product_id = ? ORDER BY r.created_at DESC";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Review> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        }
    }
    public boolean exists(long userId, long productId) throws SQLException {
        String sql = "SELECT 1 FROM reviews WHERE user_id = ? AND product_id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, productId);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }
    public Map<Long, RatingSummary> averageRatings() throws SQLException {
        String sql = "SELECT product_id, AVG(rating * 1.0) AS avg_rating, COUNT(*) AS cnt " +
                     "FROM reviews GROUP BY product_id";
        Map<Long, RatingSummary> out = new HashMap<>();
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.put(rs.getLong("product_id"),
                        new RatingSummary(rs.getDouble("avg_rating"), rs.getInt("cnt")));
            }
        }
        return out;
    }
    public RatingSummary ratingForProduct(long productId) throws SQLException {
        String sql = "SELECT AVG(rating * 1.0) AS avg_rating, COUNT(*) AS cnt " +
                     "FROM reviews WHERE product_id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new RatingSummary(rs.getDouble("avg_rating"), rs.getInt("cnt"));
            }
        }
        return new RatingSummary(0d, 0);
    }
    public long create(Review r) throws SQLException {
        String sql = "INSERT INTO reviews (product_id, user_id, rating, comment) VALUES (?, ?, ?, ?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, r.getProductId());
            ps.setLong(2, r.getUserId());
            ps.setInt(3, r.getRating());
            ps.setString(4, r.getComment());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getLong(1);
            }
        }
        throw new SQLException("Failed to retrieve generated review id");
    }
    private Review map(ResultSet rs) throws SQLException {
        Review r = new Review();
        r.setId(rs.getLong("id"));
        r.setProductId(rs.getLong("product_id"));
        r.setUserId(rs.getLong("user_id"));
        r.setUsername(rs.getString("username"));
        r.setRating(rs.getInt("rating"));
        r.setComment(rs.getString("comment"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) r.setCreatedAt(ts.toInstant());
        return r;
    }
}
