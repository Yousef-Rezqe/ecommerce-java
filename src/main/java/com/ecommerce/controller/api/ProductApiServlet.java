package com.ecommerce.controller.api;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.service.ProductService;
import com.ecommerce.util.JsonUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
@WebServlet({"/api/products", "/api/products/*"})
public class ProductApiServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(ProductApiServlet.class);
    private final ProductService products = new ProductService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info("GET {}", req.getRequestURI());
        try {
            Long id = parseId(req);
            if (id == null) {
                JsonUtil.write(resp, 200, products.listAll());
            } else {
                Optional<Product> p = products.findById(id);
                if (p.isEmpty()) JsonUtil.error(resp, 404, "Product not found");
                else JsonUtil.write(resp, 200, p.get());
            }
        } catch (Exception e) {
            log.error("Product GET failed", e);
            JsonUtil.error(resp, 500, "Could not load products");
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info("POST /api/products");
        User current = currentUser(req);
        if (current == null) { JsonUtil.error(resp, 401, "Authentication required"); return; }
        if (!current.isAdmin()) {
            log.warn("Non-admin user id={} attempted product create via API", current.getId());
            JsonUtil.error(resp, 403, "Admin only"); return;
        }
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> body = JsonUtil.MAPPER.readValue(req.getInputStream(), Map.class);
            if (body == null) { JsonUtil.error(resp, 400, "Empty request body"); return; }
            Product p = new Product();
            p.setName(asString(body.get("name")));
            p.setDescription(asString(body.get("description")));
            String price = asString(body.get("price"));
            if (price == null) throw new IllegalArgumentException("Price is required");
            p.setPrice(new BigDecimal(price));
            Object stock = body.get("stock");
            p.setStock(stock == null ? 0 : Integer.parseInt(String.valueOf(stock)));
            p.setImageUrl(asString(body.get("imageUrl")));
            p.setCreatedBy(current.getId());
            Product saved = products.create(p);
            log.info("API product created id={} by admin={}", saved.getId(), current.getId());
            JsonUtil.write(resp, 201, saved);
        } catch (ProductService.ValidationException | IllegalArgumentException e) {
            log.warn("API product create rejected: {}", e.getMessage());
            JsonUtil.error(resp, 400, e.getMessage());
        } catch (Exception e) {
            log.error("API product create failed", e);
            JsonUtil.error(resp, 500, "Could not create product");
        }
    }
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info("DELETE {}", req.getRequestURI());
        User current = currentUser(req);
        if (current == null) { JsonUtil.error(resp, 401, "Authentication required"); return; }
        if (!current.isAdmin()) { JsonUtil.error(resp, 403, "Admin only"); return; }
        try {
            Long id = parseId(req);
            if (id == null) { JsonUtil.error(resp, 400, "Missing product id"); return; }
            if (!products.delete(id)) { JsonUtil.error(resp, 404, "Product not found"); return; }
            log.info("API product deleted id={} by admin={}", id, current.getId());
            JsonUtil.write(resp, 200, Map.of("deleted", id));
        } catch (Exception e) {
            log.error("API product delete failed", e);
            JsonUtil.error(resp, 500, "Could not delete product");
        }
    }
    private Long parseId(HttpServletRequest req) {
        String pi = req.getPathInfo();
        if (pi == null || pi.equals("/")) return null;
        try { return Long.parseLong(pi.substring(1).split("/")[0]); }
        catch (NumberFormatException e) { return null; }
    }
    private User currentUser(HttpServletRequest req) {
        if (req.getAttribute("user") instanceof User u) return u;
        HttpSession s = req.getSession(false);
        if (s != null && s.getAttribute("user") instanceof User u) return u;
        return null;
    }
    private String asString(Object v) {
        if (v == null) return null;
        String s = String.valueOf(v).trim();
        return s.isEmpty() ? null : s;
    }
}
