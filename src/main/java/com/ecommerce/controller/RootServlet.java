package com.ecommerce.controller;
import com.ecommerce.model.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
@WebServlet(urlPatterns = {""})
public class RootServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(RootServlet.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info("GET /");
        resp.sendRedirect(req.getContextPath() + "/home");
    }
}
