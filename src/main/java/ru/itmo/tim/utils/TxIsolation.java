package ru.itmo.tim.utils;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@ApplicationScoped
public class TxIsolation {

    @PersistenceContext(unitName = "lab1")
    private EntityManager em;

    public void setLocalRepeatableRead() {
        em.createNativeQuery("SET LOCAL TRANSACTION ISOLATION LEVEL REPEATABLE READ")
                .executeUpdate();
    }

    public void setLocalSerializable() {
        em.createNativeQuery("SET LOCAL TRANSACTION ISOLATION LEVEL SERIALIZABLE")
                .executeUpdate();
    }

    public void setLocalReadCommitted() {
        em.createNativeQuery("SET LOCAL TRANSACTION ISOLATION LEVEL READ COMMITTED")
                .executeUpdate();
    }
}
