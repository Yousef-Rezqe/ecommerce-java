package com.ecommerce.controller.api;
import com.ecommerce.model.User;
import com.ecommerce.service.AuthService;
import com.ecommerce.util.JsonUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.Map;
@WebServlet({"/api/auth/login", "/api/auth/register"})
public class AuthApiServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(AuthApiServlet.class);
    private final AuthService auth = new AuthService();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getServletPath();
        log.info("POST {}", path);
        try {
            @SuppressWarnings("unchecked")
            Map<String, String> body = JsonUtil.MAPPER.readValue(req.getInputStream(), Map.class);
            if (body == null) { JsonUtil.error(resp, 400, "Empty request body"); return; }
            User user;
            if ("/api/auth/register".equals(path)) {
                user = auth.register(body.get("username"), body.get("email"), body.get("password"));
            } else {
                user = auth.login(body.get("email"), body.get("password"));
            }
            String token = auth.issueJwt(user);
            log.info("API auth success user={} path={}", user.getId(), path);
            JsonUtil.write(resp, 200, Map.of(
                    "token", token,
                    "user", Map.of(
                            "id", user.getId(),
                            "username", user.getUsername(),
                            "email", user.getEmail(),
                            "role", user.getRole())));
        } catch (AuthService.AuthException e) {
            log.warn("API auth rejected: {}", e.getMessage());
            JsonUtil.error(resp, 401, e.getMessage());
        } catch (Exception e) {
            log.error("API auth failure", e);
            JsonUtil.error(resp, 500, "Authentication error");
        }
    }
}
