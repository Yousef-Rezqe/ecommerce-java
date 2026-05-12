package com.ecommerce.filter;
import com.ecommerce.service.RateLimiter;
import com.ecommerce.util.JsonUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.Map;
@WebFilter(urlPatterns = {"/api/*"})
public class RateLimitFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(RateLimitFilter.class);
    private final RateLimiter limiter = new RateLimiter();
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest http = (HttpServletRequest) req;
        HttpServletResponse out = (HttpServletResponse) resp;
        String id = clientKey(http);
        RateLimiter.Result r = limiter.check(id);
        out.setHeader("X-RateLimit-Limit", String.valueOf(r.limit));
        out.setHeader("X-RateLimit-Remaining", String.valueOf(Math.max(0, r.limit - r.current)));
        if (!r.allowed) {
            log.warn("Rate limit exceeded for {} (current={}, limit={})", id, r.current, r.limit);
            out.setHeader("Retry-After", String.valueOf(r.retryAfterSeconds));
            JsonUtil.write(out, 429, Map.of(
                    "error", "Rate limit exceeded",
                    "retryAfterSeconds", r.retryAfterSeconds,
                    "limit", r.limit));
            return;
        }
        chain.doFilter(req, resp);
    }
    private String clientKey(HttpServletRequest http) {
        Object u = http.getAttribute("user");
        if (u != null) return "u:" + u.hashCode();
        if (http.getSession(false) != null && http.getSession(false).getAttribute("user") != null)
            return "s:" + http.getSession(false).getId();
        String xff = http.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) return "ip:" + xff.split(",")[0].trim();
        return "ip:" + http.getRemoteAddr();
    }
}
