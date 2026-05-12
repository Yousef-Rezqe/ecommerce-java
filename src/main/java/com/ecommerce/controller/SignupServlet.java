package com.ecommerce.controller;
import com.ecommerce.model.User;
import com.ecommerce.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
@WebServlet("/signup")
public class SignupServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(SignupServlet.class);
    private final AuthService auth = new AuthService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log.info("GET /signup");
        req.getRequestDispatcher("/WEB-INF/jsp/signup.jsp").forward(req, resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log.info("POST /signup");
        try {
            User u = auth.register(
                    req.getParameter("username"),
                    req.getParameter("email"),
                    req.getParameter("password"));
            req.getSession(true).setAttribute("user", u);
            log.info("User registered id={} username={}", u.getId(), u.getUsername());
            resp.sendRedirect(req.getContextPath() + "/home");
        } catch (AuthService.AuthException e) {
            log.warn("Signup rejected: {}", e.getMessage());
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/jsp/signup.jsp").forward(req, resp);
        } catch (Exception e) {
            log.error("Signup failed", e);
            Throwable root = e; while (root.getCause() != null && root.getCause() != root) root = root.getCause();
            req.setAttribute("error", "Registration failed: " + root.getClass().getSimpleName()
                    + (root.getMessage() != null ? " — " + root.getMessage() : ""));
            req.getRequestDispatcher("/WEB-INF/jsp/signup.jsp").forward(req, resp);
        }
    }
}
