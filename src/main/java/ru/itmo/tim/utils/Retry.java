package ru.itmo.tim.utils;
import java.sql.SQLException;
import javax.persistence.PersistenceException;

public final class Retry {
    private Retry() {}

    public static <T> T run(int maxAttempts, long initialDelayMs,
            double multiplier, CheckedSupplier<T> body) {
        long delay = initialDelayMs;
        Throwable last = null;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                return body.get();
            } catch (Throwable t) {
                last = t;

                if (!isRetryable(t) || attempt == maxAttempts) {
                    sneakyThrow(t);
                }

                sleep(delay);
                delay = (long) Math.ceil(delay * multiplier);
            }
        }
        sneakyThrow(last);
        return null;
    }

    private static boolean isRetryable(Throwable t) {
        SQLException sql = findSqlException(t);
        if (sql != null) {
            String state = sql.getSQLState();
            if ("40001".equals(state) || "40P01".equals(state)) {
                return true;
            }
        }
        if (t instanceof javax.transaction.RollbackException) return true;
        if (t instanceof PersistenceException) return true;
        return false;
    }
    private static SQLException findSqlException(Throwable t) {
        Throwable cur = t;
        while (cur != null) {
            if (cur instanceof SQLException) return (SQLException) cur;
            cur = cur.getCause();
        }
        return null;
    }
    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Retry interrupted", ie);
        }
    }
    @SuppressWarnings("unchecked")
    private static <E extends Throwable> void sneakyThrow(Throwable t) throws E {
        throw (E) t;
    }
    @FunctionalInterface
    public interface CheckedSupplier<T> {
        T get() throws Throwable;
    }
}
