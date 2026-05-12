package com.ecommerce.service;
import com.ecommerce.dao.UserDAO;
import com.ecommerce.model.User;
import com.ecommerce.util.JwtUtil;
import com.ecommerce.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.SQLException;
import java.util.Optional;
import java.util.regex.Pattern;
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private static final Pattern EMAIL = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    private static final Pattern USERNAME = Pattern.compile("^[A-Za-z0-9._-]{3,64}$");
    private final UserDAO users = new UserDAO();
    public static class AuthException extends RuntimeException {
        public AuthException(String message) { super(message); }
    }
    public User register(String username, String email, String password) throws SQLException {
        validate(username, email, password);
        username = username.trim();
        email = email.trim().toLowerCase();
        if (users.existsByUsernameOrEmail(username, email))
            throw new AuthException("Username or email already in use");
        User u = new User();
        u.setUsername(username);
        u.setEmail(email);
        u.setPasswordHash(PasswordUtil.hash(password));
        u.setRole("USER");
        u.setId(users.create(u));
        u.setPasswordHash(null);
        log.info("Registered user id={} username={}", u.getId(), u.getUsername());
        return u;
    }
    public User login(String email, String password) throws SQLException {
        if (email == null || password == null) throw new AuthException("Invalid credentials");
        Optional<User> opt = users.findByEmail(email.trim().toLowerCase());
        if (opt.isEmpty() || !PasswordUtil.verify(password, opt.get().getPasswordHash()))
            throw new AuthException("Invalid credentials");
        User u = opt.get();
        u.setPasswordHash(null);
        log.info("Authenticated user id={}", u.getId());
        return u;
    }
    public String issueJwt(User u) { return JwtUtil.issue(u.getId(), u.getUsername(), u.getRole()); }
    public boolean deleteAccount(long userId) throws SQLException {
        boolean ok = users.delete(userId);
        log.info("Deleted account id={} ok={}", userId, ok);
        return ok;
    }
    public Optional<User> findById(long id) throws SQLException { return users.findById(id); }
    private void validate(String username, String email, String password) {
        if (username == null || !USERNAME.matcher(username.trim()).matches())
            throw new AuthException("Username must be 3-64 chars (letters, digits, . _ -)");
        if (email == null || email.length() > 128 || !EMAIL.matcher(email.trim()).matches())
            throw new AuthException("Invalid email");
        if (password == null || password.length() < 6 || password.length() > 100)
            throw new AuthException("Password must be 6-100 characters");
    }
}
