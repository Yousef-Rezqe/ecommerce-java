package com.ecommerce.util;
import com.ecommerce.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;
public final class RedisUtil {
    private static final Logger log = LoggerFactory.getLogger(RedisUtil.class);
    private static final long COOLDOWN_MS = 30_000L;
    private static volatile JedisPool pool;
    private static volatile long unavailableUntil = 0L;
    private static volatile boolean outageReported = false;
    private RedisUtil() {}
    public static boolean isAvailable() { return System.currentTimeMillis() >= unavailableUntil; }
    public static Jedis get() { return pool().getResource(); }
    public static void markUnavailable(Throwable cause) {
        unavailableUntil = System.currentTimeMillis() + COOLDOWN_MS;
        if (!outageReported) {
            log.warn("Redis unavailable — caching disabled for {}s: {}",
                    COOLDOWN_MS / 1000, cause == null ? "?" : cause.getMessage());
            outageReported = true;
        }
    }
    public static void noteSuccess() {
        if (outageReported) {
            log.info("Redis recovered — caching re-enabled");
            outageReported = false;
        }
    }
    public static void shutdown() {
        if (pool != null) {
            try { pool.close(); } catch (JedisException ignored) {}
            pool = null;
        }
    }
    private static JedisPool pool() {
        JedisPool p = pool;
        if (p == null) {
            synchronized (RedisUtil.class) {
                if (pool == null) {
                    JedisPoolConfig cfg = new JedisPoolConfig();
                    cfg.setMaxTotal(32); cfg.setMaxIdle(8); cfg.setMinIdle(0);
                    String host = AppConfig.get("redis.host", "localhost");
                    int port = AppConfig.getInt("redis.port", 6379);
                    String pw = AppConfig.get("redis.password");
                    pool = (pw != null && !pw.isBlank())
                            ? new JedisPool(cfg, host, port, 2000, pw)
                            : new JedisPool(cfg, host, port, 2000);
                }
                p = pool;
            }
        }
        return p;
    }
}
