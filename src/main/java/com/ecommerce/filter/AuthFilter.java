package com.ecommerce.filter;
import com.ecommerce.model.User;
import com.ecommerce.util.JsonUtil;
import com.ecommerce.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.Set;
@WebFilter(urlPatterns = {"/*"})
public class AuthFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);
    private static final Set<String> PUBLIC_EXACT  = Set.of("/", "/login", "/signup", "/error", "/home", "/shop", "/contact");
    private static final Set<String> PUBLIC_PREFIX = Set.of("/css/", "/js/", "/images/", "/api/auth/", "/products/");
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest http = (HttpServletRequest) req;
        HttpServletResponse out = (HttpServletResponse) resp;
        String path = http.getRequestURI().substring(http.getContextPath().length());
        if (isPublic(path)) { chain.doFilter(req, resp); return; }
        HttpSession session = http.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            chain.doFilter(req, resp); return;
        }
        String header = http.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring("Bearer ".length()).trim();
            try {
                Claims c = JwtUtil.parse(token);
                User u = new User();
                u.setId(Long.parseLong(c.getSubject()));
                u.setUsername(c.get("username", String.class));
                u.setRole(c.get("role", String.class));
                http.setAttribute("user", u);
                chain.doFilter(req, resp);
                return;
            } catch (Exception e) {
                log.debug("JWT validation failed: {}", e.getMessage());
            }
        }
        log.info("Unauthenticated request {} -> redirecting/401", path);
        if (path.startsWith("/api/")) {
            JsonUtil.error(out, HttpServletResponse.SC_UNAUTHORIZED, "Authentication required");
        } else {
            out.sendRedirect(http.getContextPath() + "/signup");
        }
    }
    private boolean isPublic(String path) {
        if (PUBLIC_EXACT.contains(path)) return true;
        for (String p : PUBLIC_PREFIX) if (path.startsWith(p)) return true;
        return false;
    }
}
