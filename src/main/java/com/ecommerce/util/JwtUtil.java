package com.ecommerce.util;
import com.ecommerce.config.AppConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
public final class JwtUtil {
    private static final SecretKey KEY = Keys.hmacShaKeyFor(
            AppConfig.get("jwt.secret").getBytes(StandardCharsets.UTF_8));
    private static final long EXPIRY_MS = AppConfig.getInt("jwt.expiry.minutes", 60) * 60_000L;
    private JwtUtil() {}
    public static String issue(long userId, String username, String role) {
        Date now = new Date();
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("username", username)
                .claim("role", role)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + EXPIRY_MS))
                .signWith(KEY)
                .compact();
    }
    public static Claims parse(String token) {
        return Jwts.parser().verifyWith(KEY).build().parseSignedClaims(token).getPayload();
    }
}
