package com.ecommerce.controller;
import com.ecommerce.util.DBUtil;
import com.ecommerce.util.RedisUtil;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@WebListener
public class AppLifecycleListener implements ServletContextListener {
    private static final Logger log = LoggerFactory.getLogger(AppLifecycleListener.class);
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String v = String.valueOf(System.currentTimeMillis());
        sce.getServletContext().setAttribute("buildVersion", v);
        log.info("ecommerce app starting (buildVersion={})", v);
    }
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("ecommerce app stopping");
        try { DBUtil.shutdown(); } catch (Exception ignored) {}
        try { RedisUtil.shutdown(); } catch (Exception ignored) {}
    }
}
