package ru.itmo.tim.dao;

import ru.itmo.tim.DatabaseInitializier;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public abstract class GenericDao<T> {

    private final Class<T> entityClass;
    protected GenericDao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    protected EntityManager getEntityManager(){
        return DatabaseInitializier.getEntityManager();
    }
    public T find(Object id) {
        EntityManager entityManager = getEntityManager();
        try {
            return entityManager.find(entityClass, id);
        } finally{
            entityManager.close();
        }
    }
    public void save(T entity) {
        EntityManager entityManager = getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(entity);
            entityManager.getTransaction().commit();
        }catch(Exception e){
            if (entityManager.getTransaction().isActive()){
                entityManager.getTransaction().rollback();
            }
            throw e;
        }finally {
            entityManager.close();
        }
    }
    public void save(EntityManager entityManager, T entity) {
        entityManager.persist(entity);
    }
    public T find(EntityManager em, Object id){
        return em.find(entityClass,id);
    }
    public void update(EntityManager em, T entity){
        em.merge(entity);
    }
    public void delete(EntityManager em, T entity){
        em.remove(entity);
    }
    public void delete(T entity) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.remove(em.merge(entity));
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
    public void update(T entity){
        EntityManager entityManager = getEntityManager();
        try{
            entityManager.getTransaction().begin();
            T merged = entityManager.merge(entity);
            entityManager.getTransaction().commit();
        }catch(Exception e){
            if (entityManager.getTransaction().isActive()){
                entityManager.getTransaction().rollback();
            }
            throw e;
        }finally{
            entityManager.close();
        }
    }
}
