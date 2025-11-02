package app.dao;

import app.BuilderQueryForGetAll;
import app.entities.Worker;
import java.util.List;
import java.time.LocalDate;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

@Stateless
public class WorkerDao extends GenericDao<Worker> {

    public WorkerDao() {
        super(Worker.class);
    }

    public List<Worker> getAll(int page, int size) {
        int start = Math.max(0, (page - 1) * size);
        return entityManager.createQuery("SELECT w FROM Worker w", Worker.class)
                .setFirstResult(start)
                .setMaxResults(size)
                .getResultList();
    }
    public List<Worker> getAll(int page, int size, Map<String, Object> filters, String sortColumn, boolean asc) {
        StringBuilder queryBuilder = new StringBuilder("SELECT w FROM Worker w WHERE 1=1");
        queryBuilder.append(BuilderQueryForGetAll.buildWhereClause(filters, "w"));
        if (sortColumn != null && !sortColumn.isEmpty()) {
            queryBuilder.append(" ORDER BY w.").append(sortColumn);
            queryBuilder.append(asc ? " ASC" : " DESC");
        }
        TypedQuery<Worker> query = entityManager.createQuery(queryBuilder.toString(), Worker.class);
        BuilderQueryForGetAll.setQueryParameters(query,filters);
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);

        return query.getResultList();
    }
    public long countAll(Map<String, Object> filters) {
        StringBuilder queryBuilder = new StringBuilder("SELECT COUNT(w) FROM Worker w WHERE 1=1");
        queryBuilder.append(BuilderQueryForGetAll.buildWhereClause(filters, "w"));
        TypedQuery<Long> query = entityManager.createQuery(queryBuilder.toString(), Long.class);
        BuilderQueryForGetAll.setQueryParameters(query,filters);
        return query.getSingleResult();
    }
    public Double sumRatings() {
        return (Double) entityManager.createNativeQuery("SELECT sum_worker_ratings()").getSingleResult();
    }
    public List<Worker> findByNamePrefix(String prefix) {
        return entityManager.createNativeQuery("SELECT * FROM find_workers_by_name_prefix(CAST(?1 AS TEXT))", Worker.class)
                .setParameter(1, prefix)
                .getResultList();
    }

    public List<Worker> findByEndDateAfter(LocalDate date) {
        return entityManager.createNativeQuery("SELECT * FROM find_workers_by_end_date(?1)", Worker.class)
                .setParameter(1, date)
                .getResultList();
    }
    public List<Worker> getUnemployedWorkers(){
        return entityManager.createNativeQuery("SELECT * FROM find_available_workers()", Worker.class)
                .getResultList();
    }
    public void hireWorker(int workerId, Long orgId) {
        entityManager.createNativeQuery("CALL hire_worker(?1, ?2)")
                .setParameter(1, workerId)
                .setParameter(2, orgId)
                .executeUpdate();
    }

    public void fireWorker(int workerId) {
        entityManager.createNativeQuery("CALL fire_worker(?1)")
                .setParameter(1, workerId)
                .executeUpdate();
    }
    public List<Worker> findByOrganization(Long organizationId) {
        return entityManager.createNativeQuery("SELECT * FROM find_workers_by_organization(?1)", Worker.class)
                .setParameter(1, organizationId)
                .getResultList();
    }
    public Worker findByPersonId(long personId) {
        try {
            return entityManager.createQuery(
                            "SELECT w FROM Worker w WHERE w.person.id = :pid", Worker.class)
                    .setParameter("pid", personId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


}

