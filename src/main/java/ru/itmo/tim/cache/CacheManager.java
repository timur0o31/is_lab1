package ru.itmo.tim.cache;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@ApplicationScoped
public class CacheManager {
    private volatile boolean statisticsLoggingEnabled = false;

    public boolean isStatisticsLoggingEnabled() {
        return statisticsLoggingEnabled;
    }

    public void enableStatisticsLogging() {
        statisticsLoggingEnabled = true;
    }

    public void disableStatisticsLogging() {
        statisticsLoggingEnabled = false;
    }
}
