package com.ecommerce.controller;

import com.ecommerce.dao.OrderDAO;
import com.ecommerce.dao.ProductDAO;
import com.ecommerce.dao.ReviewDAO;
import com.ecommerce.dao.UserDAO;
import com.ecommerce.model.User;
import com.ecommerce.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet({"/admin", "/admin/delete-user", "/admin/create-user"})
public class AdminServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(AdminServlet.class);
    private final UserDAO userDAO = new UserDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final ReviewDAO reviewDAO = new ReviewDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!isAdmin(req)) { resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Admin only"); return; }
        log.info("GET /admin");
        try {
            req.setAttribute("users", userDAO.findAll());
            req.setAttribute("products", productDAO.findAll());
            req.setAttribute("totalReviews", reviewDAO.averageRatings().size());
            req.setAttribute("allOrders", orderDAO.findAll());
            HttpSession s = req.getSession(false);
            if (s != null) {
                req.setAttribute("adminFlash", s.getAttribute("adminFlash"));
                req.setAttribute("adminError", s.getAttribute("adminError"));
                s.removeAttribute("adminFlash");
                s.removeAttribute("adminError");
            }
            req.getRequestDispatcher("/WEB-INF/jsp/admin.jsp").forward(req, resp);
        } catch (Exception e) {
            log.error("Admin dashboard failed", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to load admin dashboard");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!isAdmin(req)) { resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Admin only"); return; }

        String path = req.getServletPath() + (req.getPathInfo() != null ? req.getPathInfo() : "");

        if (path.contains("create-user")) {
            String username = req.getParameter("username");
            String email    = req.getParameter("email");
            String password = req.getParameter("password");
            String role     = req.getParameter("role");
            if (!"USER".equals(role) && !"ADMIN".equals(role)) role = "USER";

            try {
                User created = authService.register(username, email, password);
                if ("ADMIN".equals(role)) {
                    userDAO.updateRole(created.getId(), "ADMIN");
                }
                log.info("Admin created user id={} username={} role={}", created.getId(), created.getUsername(), role);
                HttpSession s = req.getSession(true);
                s.setAttribute("adminFlash", "User \"" + created.getUsername() + "\" created successfully.");
            } catch (AuthService.AuthException e) {
                log.warn("Admin create-user rejected: {}", e.getMessage());
                HttpSession s = req.getSession(true);
                s.setAttribute("adminError", e.getMessage());
            } catch (Exception e) {
                log.error("Admin create-user failed", e);
                HttpSession s = req.getSession(true);
                s.setAttribute("adminError", "Failed to create user: " + e.getMessage());
            }
            resp.sendRedirect(req.getContextPath() + "/admin#users");
            return;
        }

        if (path.contains("delete-user")) {
            String userIdStr = req.getParameter("userId");
            if (userIdStr != null) {
                try {
                    long userId = Long.parseLong(userIdStr);
                    authService.deleteAccount(userId);
                    log.info("Admin deleted user id={}", userId);
                } catch (Exception e) {
                    log.error("Admin delete user failed", e);
                }
            }
            resp.sendRedirect(req.getContextPath() + "/admin#users");
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/admin");
    }

    private boolean isAdmin(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        if (s != null && s.getAttribute("user") instanceof User u) return u.isAdmin();
        if (req.getAttribute("user") instanceof User u) return u.isAdmin();
        return false;
    }
}
