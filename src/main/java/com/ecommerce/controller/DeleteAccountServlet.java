package com.ecommerce.controller;
import com.ecommerce.model.User;
import com.ecommerce.service.AuthService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
@WebServlet("/account/delete")
public class DeleteAccountServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(DeleteAccountServlet.class);
    private final AuthService auth = new AuthService();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info("POST /account/delete");
        HttpSession s = req.getSession(false);
        Object attr = s == null ? req.getAttribute("user") : s.getAttribute("user");
        if (!(attr instanceof User u)) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        try {
            auth.deleteAccount(u.getId());
            if (s != null) s.invalidate();
            log.info("Account deleted id={}", u.getId());
            resp.sendRedirect(req.getContextPath() + "/login");
        } catch (Exception e) {
            log.error("Account delete failed", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to delete account");
        }
    }
}
