package ru.itmo.tim.dao;

import org.hibernate.type.StringType;
import ru.itmo.tim.utils.BuilderQueryForGetAll;
import ru.itmo.tim.entity.Worker;
import java.util.List;
import java.time.LocalDate;
import java.util.Map;
import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

@ApplicationScoped
public class WorkerDao extends GenericDao<Worker> {

    public WorkerDao() {
        super(Worker.class);
    }

    public List<Worker> getAll(int page, int size) {
        var entityManager = getEntityManager();
        try {
            int start = Math.max(0, (page - 1) * size);
            return entityManager.createQuery("SELECT w FROM Worker w", Worker.class)
                    .setFirstResult(start)
                    .setMaxResults(size)
                    .getResultList();
        }finally{
            entityManager.close();
        }
    }
    public List<Worker> getAll(int page, int size, Map<String, Object> filters, String sortColumn, boolean sortDirection) {
        var entityManager = getEntityManager();
        try {
            String queryBuilder = BuilderQueryForGetAll.buildQuery("w", "Worker", filters, sortColumn, sortDirection);
            TypedQuery<Worker> query = entityManager.createQuery(queryBuilder, Worker.class);
            BuilderQueryForGetAll.setQueryParameters(query, filters);
            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);
            return query.getResultList();
        } finally {
            entityManager.close();
        }
    }
    public long countAll(Map<String, Object> filters) {
        var entityManager = getEntityManager();
        try {
            StringBuilder queryBuilder = new StringBuilder("SELECT COUNT(w) FROM Worker w WHERE 1=1");
            queryBuilder.append(BuilderQueryForGetAll.buildWhereClause(filters, "w"));
            TypedQuery<Long> query = entityManager.createQuery(queryBuilder.toString(), Long.class);
            BuilderQueryForGetAll.setQueryParameters(query, filters);
            return query.getSingleResult();
        } finally {
            entityManager.close();
        }
    }
    public Double sumRatings() {
        var entityManager = getEntityManager();
        try {
            return (Double) entityManager.createNativeQuery("SELECT sum_worker_ratings()").getSingleResult();
        } finally {
            entityManager.close();
        }
    }
    public List<Worker> findByNamePrefix(String prefix) {
        var entityManager = getEntityManager();
        try {
            return entityManager.createNativeQuery("SELECT * FROM find_workers_by_name_prefix(CAST(?1 AS TEXT))", Worker.class)
                    .setParameter(1, prefix)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    public List<Worker> findByEndDateAfter(LocalDate date) {
        var entityManager = getEntityManager();
        try {
            return entityManager.createNativeQuery("SELECT * FROM find_workers_by_end_date(?1)", Worker.class)
                    .setParameter(1, date)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }
    public List<Worker> getUnemployedWorkers(){
        var entityManager = getEntityManager();
        try {
            return entityManager.createNativeQuery("SELECT * FROM find_available_workers()", Worker.class)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }
    public boolean hireWorker(Long personId, Long orgId, String name, Float salary, int rating, String position, Long x, Integer y) {
        var entityManager = getEntityManager();
        try {
            return (boolean) entityManager.createNativeQuery("SELECT hire_worker(:person, :org, :name, :salary, :rating, CAST(:position AS VARCHAR), :x, :y)")
                    .setParameter("person", personId)
                    .setParameter("org", orgId)
                    .setParameter("name", name)
                    .setParameter("salary", salary)
                    .setParameter("rating", rating)
                    .setParameter("position", position)
                    .setParameter("x", x)
                    .setParameter("y", y)
                    .getSingleResult();
        } finally {
            entityManager.close();
        }
    }

    public void fireWorker(int workerId) {
        var entityManager = getEntityManager();
        try {
            entityManager.createNativeQuery("CALL fire_worker(?1)")
                    .setParameter(1, workerId)
                    .executeUpdate();
        } finally {
            entityManager.close();
        }
    }
    public List<Worker> findByOrganization(Long organizationId) {
        var entityManager = getEntityManager();
        try {
            return entityManager.createNativeQuery("SELECT * FROM find_workers_by_organization(?1)", Worker.class)
                    .setParameter(1, organizationId)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }
    public Worker findByPersonId(long personId) {
        var entityManager = getEntityManager();
        try {
            return entityManager.createQuery(
                            "SELECT w FROM Worker w WHERE w.person.id = :pid", Worker.class)
                    .setParameter("pid", personId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally{
            entityManager.close();
        }
    }
}

