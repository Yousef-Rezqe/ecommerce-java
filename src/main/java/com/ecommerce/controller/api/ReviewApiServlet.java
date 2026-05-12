package com.ecommerce.controller.api;
import com.ecommerce.model.User;
import com.ecommerce.service.ReviewService;
import com.ecommerce.util.JsonUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.Map;
@WebServlet("/api/reviews")
public class ReviewApiServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(ReviewApiServlet.class);
    private final ReviewService reviews = new ReviewService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info("GET /api/reviews");
        try {
            String pid = req.getParameter("productId");
            if (pid == null) JsonUtil.write(resp, 200, reviews.recent());
            else JsonUtil.write(resp, 200, reviews.forProduct(Long.parseLong(pid)));
        } catch (NumberFormatException e) {
            JsonUtil.error(resp, 400, "Invalid productId");
        } catch (Exception e) {
            log.error("Review GET failed", e);
            JsonUtil.error(resp, 500, "Could not load reviews");
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info("POST /api/reviews");
        User current = currentUser(req);
        if (current == null) { JsonUtil.error(resp, 401, "Authentication required"); return; }
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> body = JsonUtil.MAPPER.readValue(req.getInputStream(), Map.class);
            if (body == null) { JsonUtil.error(resp, 400, "Empty request body"); return; }
            if (body.get("productId") == null) { JsonUtil.error(resp, 400, "productId required"); return; }
            if (body.get("rating") == null) { JsonUtil.error(resp, 400, "rating required"); return; }
            long productId = Long.parseLong(String.valueOf(body.get("productId")));
            int rating = Integer.parseInt(String.valueOf(body.get("rating")));
            String comment = (String) body.get("comment");
            JsonUtil.write(resp, 201, reviews.create(current.getId(), productId, rating, comment));
        } catch (ReviewService.ValidationException | NumberFormatException e) {
            log.warn("API review rejected: {}", e.getMessage());
            JsonUtil.error(resp, 400, e.getMessage());
        } catch (Exception e) {
            log.error("API review failed", e);
            JsonUtil.error(resp, 500, "Could not create review");
        }
    }
    private User currentUser(HttpServletRequest req) {
        if (req.getAttribute("user") instanceof User u) return u;
        HttpSession s = req.getSession(false);
        if (s != null && s.getAttribute("user") instanceof User u) return u;
        return null;
    }
}
