package com.ecommerce.service;
import com.ecommerce.config.AppConfig;
import com.ecommerce.dao.ProductDAO;
import com.ecommerce.model.Product;
import com.ecommerce.util.JsonUtil;
import com.ecommerce.util.RedisUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
public class ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
    private static final String KEY_ALL = "products:all";
    private static final String KEY_ONE = "products:";
    private final ProductDAO products = new ProductDAO();
    private final int ttlSeconds = AppConfig.getInt("cache.products.ttl.seconds", 120);
    public static class ValidationException extends RuntimeException {
        public ValidationException(String m) { super(m); }
    }
    public List<Product> listAll() throws SQLException {
        if (!RedisUtil.isAvailable()) return products.findAll();
        try (Jedis j = RedisUtil.get()) {
            String cached = j.get(KEY_ALL);
            if (cached != null) {
                RedisUtil.noteSuccess();
                return JsonUtil.MAPPER.readValue(cached, new TypeReference<List<Product>>() {});
            }
            List<Product> list = products.findAll();
            j.setex(KEY_ALL, ttlSeconds, JsonUtil.MAPPER.writeValueAsString(list));
            RedisUtil.noteSuccess();
            return list;
        } catch (JedisException e) {
            RedisUtil.markUnavailable(e);
            return products.findAll();
        } catch (java.io.IOException e) {
            log.warn("Cache deserialization failed: {}", e.getMessage());
            return products.findAll();
        }
    }
    public Optional<Product> findById(long id) throws SQLException {
        String key = KEY_ONE + id;
        if (!RedisUtil.isAvailable()) return products.findById(id);
        try (Jedis j = RedisUtil.get()) {
            String cached = j.get(key);
            if (cached != null) {
                RedisUtil.noteSuccess();
                return Optional.of(JsonUtil.MAPPER.readValue(cached, Product.class));
            }
            Optional<Product> opt = products.findById(id);
            if (opt.isPresent())
                j.setex(key, ttlSeconds, JsonUtil.MAPPER.writeValueAsString(opt.get()));
            RedisUtil.noteSuccess();
            return opt;
        } catch (JedisException e) {
            RedisUtil.markUnavailable(e);
            return products.findById(id);
        } catch (java.io.IOException e) {
            log.warn("Cache deserialization failed: {}", e.getMessage());
            return products.findById(id);
        }
    }
    public Product create(Product p) throws SQLException {
        validate(p);
        long id = products.create(p);
        p.setId(id);
        invalidate(id);
        log.info("Product created id={} name='{}'", id, p.getName());
        return p;
    }
    public boolean update(Product p) throws SQLException {
        validate(p);
        boolean ok = products.update(p);
        invalidate(p.getId());
        log.info("Product updated id={} name='{}' ok={}", p.getId(), p.getName(), ok);
        return ok;
    }
    public boolean delete(long id) throws SQLException {
        boolean ok = products.delete(id);
        invalidate(id);
        log.info("Product deleted id={} ok={}", id, ok);
        return ok;
    }
    private void invalidate(long id) {
        if (!RedisUtil.isAvailable()) return;
        try (Jedis j = RedisUtil.get()) {
            j.del(KEY_ALL, KEY_ONE + id);
            RedisUtil.noteSuccess();
        } catch (JedisException e) {
            RedisUtil.markUnavailable(e);
        }
    }
    private void validate(Product p) {
        if (p.getName() == null || p.getName().isBlank())
            throw new ValidationException("Name is required");
        if (p.getName().length() > 150)
            throw new ValidationException("Name too long (max 150)");
        if (p.getDescription() != null && p.getDescription().length() > 2000)
            throw new ValidationException("Description too long (max 2000)");
        if (p.getPrice() == null || p.getPrice().signum() < 0 || p.getPrice().compareTo(new BigDecimal("1000000")) > 0)
            throw new ValidationException("Price must be between 0 and 1,000,000");
        if (p.getStock() < 0 || p.getStock() > 1_000_000)
            throw new ValidationException("Stock must be between 0 and 1,000,000");
        if (p.getImageUrl() != null && p.getImageUrl().length() > 500)
            throw new ValidationException("Image URL too long (max 500)");
    }
}
