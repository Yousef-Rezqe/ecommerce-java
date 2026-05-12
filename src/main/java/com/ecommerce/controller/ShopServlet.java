package com.ecommerce.controller;
import com.ecommerce.dao.ProductDAO;
import com.ecommerce.model.Product;
import com.ecommerce.service.ReviewService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
@WebServlet("/shop")
public class ShopServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(ShopServlet.class);
    private final ProductDAO productDAO = new ProductDAO();
    private final ReviewService reviewService = new ReviewService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log.info("GET /shop q={}", req.getParameter("q"));
        try {
            List<Product> all = productDAO.findAll();
            String q = req.getParameter("q");
            if (q != null && !q.isBlank()) {
                String lq = q.trim().toLowerCase();
                all = all.stream()
                        .filter(p -> p.getName().toLowerCase().contains(lq)
                                || (p.getDescription() != null
                                    && p.getDescription().toLowerCase().contains(lq)))
                        .collect(Collectors.toList());
            }
            String minStr = req.getParameter("minPrice");
            String maxStr = req.getParameter("maxPrice");
            if (minStr != null && !minStr.isBlank()) {
                try {
                    BigDecimal min = new BigDecimal(minStr);
                    all = all.stream()
                            .filter(p -> p.getPrice().compareTo(min) >= 0)
                            .collect(Collectors.toList());
                } catch (NumberFormatException ignored) {}
            }
            if (maxStr != null && !maxStr.isBlank()) {
                try {
                    BigDecimal max = new BigDecimal(maxStr);
                    all = all.stream()
                            .filter(p -> p.getPrice().compareTo(max) <= 0)
                            .collect(Collectors.toList());
                } catch (NumberFormatException ignored) {}
            }
            if ("1".equals(req.getParameter("inStock"))) {
                all = all.stream().filter(p -> p.getStock() > 0).collect(Collectors.toList());
            }
            String sort = req.getParameter("sort");
            if ("price_asc".equals(sort)) {
                all.sort((a, b) -> a.getPrice().compareTo(b.getPrice()));
            } else if ("price_desc".equals(sort)) {
                all.sort((a, b) -> b.getPrice().compareTo(a.getPrice()));
            }
            req.setAttribute("products", all);
            req.setAttribute("ratings", reviewService.ratingsByProduct());
            req.getRequestDispatcher("/WEB-INF/jsp/shop.jsp").forward(req, resp);
        } catch (SQLException e) {
            log.error("Shop page failed", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to load shop");
        }
    }
}
