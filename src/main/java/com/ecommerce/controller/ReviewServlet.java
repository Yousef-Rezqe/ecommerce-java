package com.ecommerce.controller;
import com.ecommerce.model.User;
import com.ecommerce.service.ReviewService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
@WebServlet("/reviews")
public class ReviewServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(ReviewServlet.class);
    private final ReviewService reviews = new ReviewService();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info("POST /reviews");
        User current = currentUser(req);
        if (current == null) { resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Sign in required"); return; }
        String productIdStr = req.getParameter("productId");
        String ratingStr    = req.getParameter("rating");
        String comment      = req.getParameter("comment");
        if (productIdStr == null || ratingStr == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "productId and rating are required");
            return;
        }
        try {
            long productId = Long.parseLong(productIdStr);
            int rating = Integer.parseInt(ratingStr);
            reviews.create(current.getId(), productId, rating, comment);
            String back = req.getParameter("redirect");
            resp.sendRedirect(req.getContextPath() + (back != null && back.startsWith("/") ? back : "/home"));
        } catch (ReviewService.ValidationException | NumberFormatException e) {
            log.warn("Review rejected: {}", e.getMessage());
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Review submission failed", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Could not submit review");
        }
    }
    private User currentUser(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        if (s != null && s.getAttribute("user") instanceof User u) return u;
        if (req.getAttribute("user") instanceof User u) return u;
        return null;
    }
}
