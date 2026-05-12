package com.ecommerce.service;
import com.ecommerce.dao.ReviewDAO;
import com.ecommerce.dao.ReviewDAO.RatingSummary;
import com.ecommerce.model.Review;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
public class ReviewService {
    private static final Logger log = LoggerFactory.getLogger(ReviewService.class);
    private final ReviewDAO reviews = new ReviewDAO();
    public static class ValidationException extends RuntimeException {
        public ValidationException(String m) { super(m); }
    }
    public List<Review> recent() throws SQLException { return reviews.findRecent(10); }
    public List<Review> forProduct(long productId) throws SQLException {
        return reviews.findByProduct(productId);
    }
    public List<Review> byUser(long userId) throws SQLException {
        return reviews.findByUser(userId);
    }
    public Map<Long, RatingSummary> ratingsByProduct() throws SQLException {
        return reviews.averageRatings();
    }
    public RatingSummary ratingFor(long productId) throws SQLException {
        return reviews.ratingForProduct(productId);
    }
    public Review create(long userId, long productId, int rating, String comment) throws SQLException {
        if (rating < 1 || rating > 5) throw new ValidationException("Rating must be 1-5");
        if (comment != null && comment.length() > 1000) throw new ValidationException("Comment too long (max 1000)");
        if (reviews.exists(userId, productId))
            throw new ValidationException("You've already reviewed this product");
        Review r = new Review();
        r.setUserId(userId);
        r.setProductId(productId);
        r.setRating(rating);
        r.setComment(comment == null ? null : comment.trim());
        r.setId(reviews.create(r));
        log.info("Review created id={} user={} product={} rating={}", r.getId(), userId, productId, rating);
        return r;
    }
}
