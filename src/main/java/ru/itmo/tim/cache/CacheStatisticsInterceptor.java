package ru.itmo.tim.cache;

import org.hibernate.Session;
import org.hibernate.stat.Statistics;
import ru.itmo.tim.DatabaseInitializier;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.logging.Logger;

@Interceptor
@CacheStatisticsLogging
public class CacheStatisticsInterceptor {
    private static final Logger logger = Logger.getLogger(CacheStatisticsInterceptor.class.getName());
    private long totalL2Hits = 0;
    private long totalL2Misses = 0;
    private long totalQueryHits = 0;
    private long totalQueryMisses = 0;
    @PersistenceContext
    private EntityManager em;
    @Inject
    private CacheManager cacheManager;
    @AroundInvoke
    public Object logCacheStatistics(InvocationContext context) throws Exception {
        boolean shouldLog = cacheManager.isStatisticsLoggingEnabled(); // заменить на druid

        if (!shouldLog) {
            return context.proceed();
        }

        long startTime = System.currentTimeMillis();

        try {
            Object result = context.proceed();
            return result;
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;

           // logCacheStats(context.getMethod().getName(), executionTime);
        }
    }

    private void logCacheStats(String methodName, long executionTime) {
        try {
            Session session = DatabaseInitializier.getEntityManager()
                    .unwrap(Session.class);

            Statistics stats = session.getSessionFactory().getStatistics();

            long currentL2Hits = stats.getSecondLevelCacheHitCount();
            long currentL2Misses = stats.getSecondLevelCacheMissCount();
            long currentQueryHits = stats.getQueryCacheHitCount();
            long currentQueryMisses = stats.getQueryCacheMissCount();

            totalL2Hits += currentL2Hits;
            totalL2Misses += currentL2Misses;
            totalQueryHits += currentQueryHits;
            totalQueryMisses += currentQueryMisses;

            logger.info("=== Cache Stats for method: " + methodName + " ===");
            logger.info("L2 Cache Hits: " + totalL2Hits);
            logger.info("L2 Cache Misses: " + totalL2Misses);
            logger.info("Query Cache Hits: " + totalQueryHits);
            logger.info("Query Cache Misses: " + totalQueryMisses);
            logger.info("Execution time: " + executionTime + " ms");
            logger.info("==============================================");

        } catch (Exception e) {
            logger.warning("Failed to get cache stats: " + e.getMessage());
        }
    }
}
