package app.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class GenericDao<T> {
    @PersistenceContext(unitName = "lab1")
    protected EntityManager entityManager;
    private final Class<T> entityClass;
    protected GenericDao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    public T find(Object id) {
        return entityManager.find(entityClass, id);
    }
    public void save(T entity) {
        entityManager.persist(entity);
    }
    public void delete(T entity) {
        entityManager.remove(entityManager.merge(entity));
    }
    public void update(T entity){
        entityManager.merge(entity);
        entityManager.flush();
    }
}
