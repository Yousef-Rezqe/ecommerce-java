package com.ecommerce.controller;
import com.ecommerce.service.ProductService;
import com.ecommerce.service.ReviewService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(HomeServlet.class);
    private final ProductService productService = new ProductService();
    private final ReviewService reviewService = new ReviewService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log.info("GET /home");
        try {
            req.setAttribute("products", productService.listAll());
            req.setAttribute("ratings", reviewService.ratingsByProduct());
            req.setAttribute("recentReviews", reviewService.recent());
            req.getRequestDispatcher("/WEB-INF/jsp/home.jsp").forward(req, resp);
        } catch (Exception e) {
            log.error("Home rendering failed", e);
            req.setAttribute("jakarta.servlet.error.exception", e);
            req.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(req, resp);
        }
    }
}
