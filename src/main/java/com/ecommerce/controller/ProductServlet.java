package com.ecommerce.controller;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.service.ProductService;
import com.ecommerce.service.ReviewService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
@WebServlet({"/products", "/products/*"})
public class ProductServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(ProductServlet.class);
    private final ProductService products = new ProductService();
    private final ReviewService reviews = new ReviewService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log.info("GET {}", req.getRequestURI());
        String pi = req.getPathInfo();
        Long id = parseId(pi);
        if (id == null) { resp.sendRedirect(req.getContextPath() + "/home"); return; }
        boolean editMode = pi != null && pi.endsWith("/edit");
        try {
            Optional<Product> p = products.findById(id);
            if (p.isEmpty()) { resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found"); return; }
            if (editMode) {
                if (!isAdmin(req)) { resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Admin only"); return; }
                req.setAttribute("product", p.get());
                req.getRequestDispatcher("/WEB-INF/jsp/product-edit.jsp").forward(req, resp);
                return;
            }
            req.setAttribute("product", p.get());
            req.setAttribute("reviews", reviews.forProduct(id));
            req.setAttribute("rating",  reviews.ratingFor(id));
            req.getRequestDispatcher("/WEB-INF/jsp/product-details.jsp").forward(req, resp);
        } catch (Exception e) {
            log.error("Product details failed for id={}", id, e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to load product");
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info("POST {}", req.getRequestURI());
        User current = currentUser(req);
        if (current == null) { resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Sign in required"); return; }
        if (!current.isAdmin()) {
            log.warn("Non-admin user id={} attempted product mutation", current.getId());
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Admin only");
            return;
        }
        String pi = req.getPathInfo();
        try {
            if (pi != null && pi.endsWith("/delete")) {
                long id = Long.parseLong(pi.replaceAll("[^0-9]", ""));
                products.delete(id);
                log.info("Product deleted id={} by admin={}", id, current.getId());
                String ref = req.getHeader("Referer");
                if (ref != null && ref.contains("/admin")) {
                    resp.sendRedirect(req.getContextPath() + "/admin#products");
                } else {
                    resp.sendRedirect(req.getContextPath() + "/shop");
                }
            } else if (pi != null && pi.endsWith("/edit")) {
                Long id = parseId(pi);
                if (id == null) { resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing product id"); return; }
                Product p = parseProductForm(req, current.getId());
                p.setId(id);
                products.update(p);
                log.info("Product updated id={} by admin={}", id, current.getId());
                resp.sendRedirect(req.getContextPath() + "/products/" + id);
            } else {
                Product p = parseProductForm(req, current.getId());
                products.create(p);
                log.info("Product created '{}' by admin={}", p.getName(), current.getId());
                resp.sendRedirect(req.getContextPath() + "/admin#products");
            }
        } catch (ProductService.ValidationException | IllegalArgumentException e) {
            log.warn("Bad product mutation request: {}", e.getMessage());
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Product mutation failed", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Operation failed");
        }
    }
    private Product parseProductForm(HttpServletRequest req, long createdBy) {
        String name = trim(req.getParameter("name"));
        String desc = trim(req.getParameter("description"));
        String priceStr = req.getParameter("price");
        String stockStr = req.getParameter("stock");
        String imageUrl = trim(req.getParameter("imageUrl"));
        if (name == null) throw new IllegalArgumentException("Name is required");
        if (priceStr == null) throw new IllegalArgumentException("Price is required");
        if (stockStr == null) throw new IllegalArgumentException("Stock is required");
        Product p = new Product();
        p.setName(name);
        p.setDescription(desc);
        p.setPrice(new BigDecimal(priceStr));
        p.setStock(Integer.parseInt(stockStr));
        p.setImageUrl(imageUrl);
        p.setCreatedBy(createdBy);
        return p;
    }
    private Long parseId(String pi) {
        if (pi == null || pi.equals("/")) return null;
        String[] parts = pi.split("/");
        if (parts.length < 2) return null;
        try { return Long.parseLong(parts[1]); } catch (NumberFormatException e) { return null; }
    }
    private boolean isAdmin(HttpServletRequest req) {
        User u = currentUser(req);
        return u != null && u.isAdmin();
    }
    private User currentUser(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        if (s != null && s.getAttribute("user") instanceof User u) return u;
        if (req.getAttribute("user") instanceof User u) return u;
        return null;
    }
    private String trim(String s) { return (s == null || s.isBlank()) ? null : s.trim(); }
}
