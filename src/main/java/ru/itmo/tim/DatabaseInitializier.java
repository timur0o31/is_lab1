package ru.itmo.tim;

import com.alibaba.druid.pool.DruidDataSource;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@WebListener
public class DatabaseInitializier implements ServletContextListener {
    private static final Logger logger = Logger.getLogger(DatabaseInitializier.class.getName());

    private static DruidDataSource dataSource;
    private static EntityManagerFactory entityManagerFactory;


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            logger.info("Starting app init: Druid + JPA + SQL functions...");
            initDataSource();
            initEntityManagerFactory();
            runSqlFunctions(dataSource);
            ServletContext ctx = sce.getServletContext();
            ctx.setAttribute("APP_DS", dataSource);
            ctx.setAttribute("APP_EMF", entityManagerFactory);
            logger.info("App init finished successfully.");
        } catch (Exception e) {
            logger.severe("App init failed: " + e.getMessage());
            safeClose();
            throw new RuntimeException(e);
        }
    }
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Stopping app, closing resources...");
        safeClose();
    }

    private static void initDataSource() {
        try {
            logger.info("Initializing Druid DataSource...");
            String user = System.getProperty("db.user");
            String pass = System.getProperty("db.password");
            dataSource = new DruidDataSource();
            dataSource.setDriverClassName("org.postgresql.Driver");
            dataSource.setUrl("jdbc:postgresql://localhost:5432/studs");
            dataSource.setUsername(user);
            dataSource.setPassword(pass);

            dataSource.setInitialSize(2);
            dataSource.setMinIdle(2);
            dataSource.setMaxActive(10);
            dataSource.setMaxWait(30_000);
            dataSource.setTimeBetweenEvictionRunsMillis(60_000);
            dataSource.setMinEvictableIdleTimeMillis(600_000);
            dataSource.setMaxEvictableIdleTimeMillis(1_800_000);
            dataSource.setValidationQuery("SELECT 1");
            dataSource.setTestOnBorrow(true);
            dataSource.setTestWhileIdle(true);
            dataSource.setTestOnReturn(false);

            dataSource.setPoolPreparedStatements(false);

            try (Connection c = dataSource.getConnection()) {
                logger.info("Druid connection test OK: " + (c != null));
            }

            logger.info("Druid DataSource initialized successfully");

        } catch (Exception e) {
            logger.severe("Failed to initialize Druid: " + e.getMessage());
            throw new RuntimeException("Druid initialization failed", e);
        }
    }

    private static void initEntityManagerFactory() {
        try {
            logger.info("Initializing EntityManagerFactory...");

            Map<String, Object> properties = new HashMap<>();

            properties.put("javax.persistence.nonJtaDataSource", dataSource);
            properties.put("hibernate.hbm2ddl.auto", "update");
            properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL95Dialect");
            properties.put("hibernate.show_sql", "true");
            properties.put("hibernate.format_sql", "true");
            properties.put("hibernate.generate_statistics", "true");

            properties.put("hibernate.cache.use_second_level_cache", "true");
            properties.put("hibernate.cache.use_query_cache", "true");

            entityManagerFactory = Persistence.createEntityManagerFactory("lab1", properties);
            logger.info("EntityManagerFactory initialized successfully");

        } catch (Exception e) {
            logger.severe("Failed to initialize EntityManagerFactory: " + e.getMessage());
            throw new RuntimeException("EntityManagerFactory initialization failed", e);
        }
    }
    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
    public void safeClose() {
        try {
            if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
                entityManagerFactory.close();
                logger.info("EntityManagerFactory closed");
            }
        } catch (Exception ignored) {}

        try {
            if (dataSource != null) {
                dataSource.close();
                logger.info("Druid DataSource closed");
            }
        } catch (Exception ignored) {}
    }

    public void runSqlFunctions(DruidDataSource ds) {
        try{
            try (Connection conn = ds.getConnection();
                 Statement stmt = conn.createStatement()){
                try (InputStream is = getClass().getClassLoader().getResourceAsStream("sql/func.sql")) {
                if (is == null) {
                    throw new FileNotFoundException("Resource 'sql/func.sql' not found");
                }
                stmt.execute(new String(is.readAllBytes(), StandardCharsets.UTF_8));
            }
        }
    }catch(Exception e){
            e.printStackTrace();
        }
    }
}
