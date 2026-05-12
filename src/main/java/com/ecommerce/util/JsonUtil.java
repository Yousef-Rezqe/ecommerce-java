package com.ecommerce.util;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
public final class JsonUtil {
    public static final ObjectMapper MAPPER = new ObjectMapper()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    private JsonUtil() {}
    public static void write(HttpServletResponse resp, int status, Object body) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json;charset=UTF-8");
        MAPPER.writeValue(resp.getWriter(), body);
    }
    public static void error(HttpServletResponse resp, int status, String message) throws IOException {
        write(resp, status, Map.of("error", message, "status", status));
    }
}
