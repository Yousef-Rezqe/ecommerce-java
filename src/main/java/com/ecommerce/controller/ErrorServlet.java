package com.ecommerce.controller;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
@WebServlet("/error")
public class ErrorServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(ErrorServlet.class);
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Integer status = (Integer) req.getAttribute("jakarta.servlet.error.status_code");
        String  uri    = (String)  req.getAttribute("jakarta.servlet.error.request_uri");
        String  msg    = (String)  req.getAttribute("jakarta.servlet.error.message");
        Throwable t    = (Throwable) req.getAttribute("jakarta.servlet.error.exception");
        if (status == null) status = 500;
        Throwable root = t;
        if (root != null) while (root.getCause() != null && root.getCause() != root) root = root.getCause();
        if (status >= 500) {
            log.error("Error {} on {}: {}", status, uri, msg, t);
        } else {
            log.warn("Error {} on {}: {}", status, uri, msg);
        }
        resp.setStatus(status);
        req.setAttribute("statusCode", status);
        req.setAttribute("failedUri",  uri);
        req.setAttribute("errorMessage", msg);
        req.setAttribute("rootClass",   root == null ? null : root.getClass().getName());
        req.setAttribute("rootMessage", root == null ? null : root.getMessage());
        req.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(req, resp);
    }
}
