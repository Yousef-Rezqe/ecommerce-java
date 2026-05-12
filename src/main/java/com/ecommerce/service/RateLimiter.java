package com.ecommerce.service;
import com.ecommerce.config.AppConfig;
import com.ecommerce.util.RedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;
public class RateLimiter {
    private final int max = AppConfig.getInt("ratelimit.requests", 60);
    private final int windowSeconds = AppConfig.getInt("ratelimit.window.seconds", 60);
    public static class Result {
        public final boolean allowed;
        public final long current;
        public final int limit;
        public final int retryAfterSeconds;
        public Result(boolean allowed, long current, int limit, int retryAfterSeconds) {
            this.allowed = allowed; this.current = current;
            this.limit = limit; this.retryAfterSeconds = retryAfterSeconds;
        }
    }
    public Result check(String identifier) {
        if (!RedisUtil.isAvailable()) return new Result(true, 0, max, 0);
        String key = "rl:" + identifier;
        try (Jedis j = RedisUtil.get()) {
            long count = j.incr(key);
            if (count == 1L) j.expire(key, windowSeconds);
            RedisUtil.noteSuccess();
            if (count > max) {
                Long ttl = j.ttl(key);
                int retry = (ttl == null || ttl < 0) ? windowSeconds : ttl.intValue();
                return new Result(false, count, max, retry);
            }
            return new Result(true, count, max, 0);
        } catch (JedisException e) {
            RedisUtil.markUnavailable(e);
            return new Result(true, 0, max, 0);
        }
    }
}
