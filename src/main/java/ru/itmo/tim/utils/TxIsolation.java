package ru.itmo.tim.utils;

import javax.persistence.EntityManager;

public final class TxIsolation {
    public enum Level {
        READ_COMMITTED("READ COMMITTED"),
        REPEATABLE_READ("REPEATABLE READ"),
        SERIALIZABLE("SERIALIZABLE");

        private final String sql;

        Level(String sql) {
            this.sql = sql;
        }
    }

    private TxIsolation() {}

    public static void setLocal(EntityManager em, Level level) {
        if (level == null) {
            return;
        }
        em.createNativeQuery("SET LOCAL TRANSACTION ISOLATION LEVEL " + level.sql)
                .executeUpdate();
    }
}
