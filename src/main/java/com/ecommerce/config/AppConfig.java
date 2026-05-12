package com.ecommerce.config;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
public final class AppConfig {
    private static final Properties PROPS = new Properties();
    static {
        try (InputStream in = AppConfig.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (in == null) throw new IllegalStateException("application.properties not found on classpath");
            PROPS.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load application.properties", e);
        }
    }
    private AppConfig() {}
    public static String get(String key) {
        String env = System.getenv(toEnv(key));
        if (env != null && !env.isBlank()) return env;
        return PROPS.getProperty(key);
    }
    public static String get(String key, String defaultValue) {
        String v = get(key);
        return v == null ? defaultValue : v;
    }
    public static int getInt(String key, int defaultValue) {
        try {
            String v = get(key);
            return v == null ? defaultValue : Integer.parseInt(v.trim());
        } catch (NumberFormatException e) { return defaultValue; }
    }
    private static String toEnv(String key) { return key.toUpperCase().replace('.', '_'); }
}
