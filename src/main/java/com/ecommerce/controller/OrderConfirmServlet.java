package com.ecommerce.controller;

import com.ecommerce.dao.OrderDAO;
import com.ecommerce.model.Order;
import com.ecommerce.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

@WebServlet("/order/confirm")
public class OrderConfirmServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(OrderConfirmServlet.class);
    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = currentUser(req);
        if (user == null) { resp.sendRedirect(req.getContextPath() + "/login"); return; }

        String idStr = req.getParameter("id");
        try {
            List<Order> orders = orderDAO.findByUser(user.getId());
            Order found = null;
            if (idStr != null) {
                long id = Long.parseLong(idStr);
                found = orders.stream().filter(o -> o.getId() == id).findFirst().orElse(null);
            }
            if (found == null && !orders.isEmpty()) found = orders.get(0);
            req.setAttribute("order", found);
            req.getRequestDispatcher("/WEB-INF/jsp/order-confirm.jsp").forward(req, resp);
        } catch (Exception e) {
            log.error("Order confirm page failed", e);
            resp.sendError(500, "Unable to load confirmation");
        }
    }

    private User currentUser(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        if (s != null && s.getAttribute("user") instanceof User u) return u;
        if (req.getAttribute("user") instanceof User u) return u;
        return null;
    }
}
