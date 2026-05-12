package com.ecommerce.controller;
import com.ecommerce.dao.OrderDAO;
import com.ecommerce.model.User;
import com.ecommerce.service.AuthService;
import com.ecommerce.service.ReviewService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.Optional;
@WebServlet("/account")
public class AccountServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(AccountServlet.class);
    private final AuthService auth = new AuthService();
    private final ReviewService reviews = new ReviewService();
    private final OrderDAO orderDAO = new OrderDAO();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log.info("GET /account");
        User current = currentUser(req);
        if (current == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        if (current.isAdmin()) {
            resp.sendRedirect(req.getContextPath() + "/admin");
            return;
        }
        try {
            Optional<User> full = auth.findById(current.getId());
            req.setAttribute("account", full.orElse(current));
            req.setAttribute("myReviews", reviews.byUser(current.getId()));
            req.setAttribute("myOrders", orderDAO.findByUser(current.getId()));
            req.getRequestDispatcher("/WEB-INF/jsp/account.jsp").forward(req, resp);
        } catch (Exception e) {
            log.error("Account page failed for user={}", current.getId(), e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to load account");
        }
    }
    private User currentUser(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        if (s != null && s.getAttribute("user") instanceof User u) return u;
        if (req.getAttribute("user") instanceof User u) return u;
        return null;
    }
}
