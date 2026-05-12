package com.ecommerce.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet("/contact")
public class ContactServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(ContactServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/contact.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String firstName = trim(req.getParameter("firstName"));
        String lastName  = trim(req.getParameter("lastName"));
        String email     = trim(req.getParameter("email"));
        String subject   = trim(req.getParameter("subject"));
        String message   = trim(req.getParameter("message"));

        if (firstName == null || email == null || message == null) {
            req.setAttribute("error", "Please fill in all required fields.");
            req.getRequestDispatcher("/WEB-INF/jsp/contact.jsp").forward(req, resp);
            return;
        }

        log.info("Contact form: from={} {} <{}> subject='{}' message='{}'",
                firstName, lastName, email, subject, message);

        req.setAttribute("success", "Thank you, " + firstName + "! We'll get back to you within 24 hours.");
        req.getRequestDispatcher("/WEB-INF/jsp/contact.jsp").forward(req, resp);
    }

    private String trim(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }
}
