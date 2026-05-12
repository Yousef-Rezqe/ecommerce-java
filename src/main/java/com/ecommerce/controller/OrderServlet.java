package com.ecommerce.controller;
import com.ecommerce.dao.OrderDAO;
import com.ecommerce.model.Order;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
@WebServlet("/order")
public class OrderServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(OrderServlet.class);
    private final ProductService productService = new ProductService();
    private final OrderDAO orderDAO = new OrderDAO();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = currentUser(req);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        String pidStr = req.getParameter("productId");
        if (pidStr == null) { resp.sendRedirect(req.getContextPath() + "/shop"); return; }
        try {
            long pid = Long.parseLong(pidStr);
            Optional<Product> p = productService.findById(pid);
            if (p.isEmpty()) { resp.sendError(404, "Product not found"); return; }
            if (p.get().getStock() == 0) {
                req.setAttribute("error", "This product is out of stock.");
                req.setAttribute("product", p.get());
                req.getRequestDispatcher("/WEB-INF/jsp/order.jsp").forward(req, resp);
                return;
            }
            String qtyStr = req.getParameter("qty");
            int qty = 1;
            try { qty = Math.max(1, Integer.parseInt(qtyStr)); } catch (Exception ignored) {}
            req.setAttribute("product", p.get());
            req.setAttribute("qty", qty);
            req.getRequestDispatcher("/WEB-INF/jsp/order.jsp").forward(req, resp);
        } catch (Exception e) {
            log.error("Order page failed", e);
            resp.sendError(500, "Unable to load order page");
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = currentUser(req);
        if (user == null) { resp.sendRedirect(req.getContextPath() + "/login"); return; }
        String pidStr   = req.getParameter("productId");
        String qtyStr   = req.getParameter("quantity");
        String fullName = trim(req.getParameter("fullName"));
        String phone    = trim(req.getParameter("phone"));
        String address  = trim(req.getParameter("address"));
        String city     = trim(req.getParameter("city"));
        String notes    = trim(req.getParameter("notes"));
        if (pidStr == null || fullName == null || phone == null || address == null || city == null) {
            resp.sendRedirect(req.getContextPath() + "/shop");
            return;
        }
        try {
            long pid = Long.parseLong(pidStr);
            int qty  = Math.max(1, Integer.parseInt(qtyStr == null ? "1" : qtyStr));
            Optional<Product> opt = productService.findById(pid);
            if (opt.isEmpty()) { resp.sendError(404, "Product not found"); return; }
            Product p = opt.get();
            if (p.getStock() < qty) {
                req.setAttribute("error", "Not enough stock. Only " + p.getStock() + " available.");
                req.setAttribute("product", p);
                req.setAttribute("qty", qty);
                req.getRequestDispatcher("/WEB-INF/jsp/order.jsp").forward(req, resp);
                return;
            }
            BigDecimal total = p.getPrice().multiply(BigDecimal.valueOf(qty));
            Order o = new Order();
            o.setUserId(user.getId());
            o.setProductId(pid);
            o.setProductName(p.getName());
            o.setQuantity(qty);
            o.setUnitPrice(p.getPrice());
            o.setTotalPrice(total);
            o.setFullName(fullName);
            o.setPhone(phone);
            o.setAddress(address);
            o.setCity(city);
            o.setNotes(notes);
            o.setStatus("PENDING");
            long orderId = orderDAO.create(o);
            log.info("Order placed id={} user={} product={} qty={} total={}",
                    orderId, user.getId(), pid, qty, total);
            resp.sendRedirect(req.getContextPath() + "/order/confirm?id=" + orderId);
        } catch (Exception e) {
            log.error("Order submission failed", e);
            resp.sendError(500, "Could not place order");
        }
    }
    private User currentUser(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        if (s != null && s.getAttribute("user") instanceof User u) return u;
        if (req.getAttribute("user") instanceof User u) return u;
        return null;
    }
    private String trim(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }
}
