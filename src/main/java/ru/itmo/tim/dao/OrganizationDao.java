package ru.itmo.tim.dao;

import ru.itmo.tim.utils.BuilderQueryForGetAll;
import ru.itmo.tim.entity.Organization;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class OrganizationDao extends GenericDao<Organization> {
    public OrganizationDao() {
        super(Organization.class);
    }
    public long countWorkers(Long orgId){
        var entityManager = getEntityManager();
        try {
            return entityManager.createQuery(
                            "SELECT COUNT(w) FROM Worker w WHERE w.organization.id = :id", Long.class)
                    .setParameter("id", orgId)
                    .getSingleResult();
        }finally{
            entityManager.close();
        }
    }
    public List<Organization> findOtherOrganizations(Long excludeId) {
        var entityManager = getEntityManager();
        try {
            return entityManager.createQuery("SELECT o FROM Organization o WHERE o.id <> :excludeId", Organization.class)
                    .setParameter("excludeId", excludeId)
                    .getResultList();
        }finally{
            entityManager.close();
        }
    }
    public List<Organization> getAll(int page, int size, String sortColumn, boolean sortDirection, Map<String, Object> filters) {
        var entityManager = getEntityManager();
        try {
            String queryBuilder = BuilderQueryForGetAll.buildQuery("o", "Organization", filters, sortColumn, sortDirection);
            TypedQuery<Organization> query = entityManager.createQuery(queryBuilder, Organization.class);
            BuilderQueryForGetAll.setQueryParameters(query, filters);
            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);
            return query.getResultList();
        }finally{
            entityManager.close();
        }
    }
    public long countAll(Map<String, Object> filters) {
        var entityManager = getEntityManager();
        try {
            StringBuilder queryBuilder = new StringBuilder("SELECT COUNT(org) FROM Organization org WHERE 1=1");
            queryBuilder.append(BuilderQueryForGetAll.buildWhereClause(filters, "org"));
            TypedQuery<Long> query = entityManager.createQuery(queryBuilder.toString(), Long.class);
            BuilderQueryForGetAll.setQueryParameters(query, filters);
            return query.getSingleResult();
        } finally {
            entityManager.close();
        }
    }
    public Organization existByName(String fullName){
        var entityManager = getEntityManager();
        try{
            TypedQuery<Organization> query = entityManager.createQuery("SELECT org FROM Organization org WHERE org.fullName= :fullName", Organization.class)
                    .setParameter("fullName", fullName);
            return query.getSingleResult();
        }catch(NoResultException e){
            return null;
        }finally{
            entityManager.close();
        }
    }
    public Organization existByName(EntityManager entityManager, String fullName){
        try{
            TypedQuery<Organization> query = entityManager.createQuery("SELECT org FROM Organization org WHERE org.fullName= :fullName", Organization.class)
                    .setParameter("fullName", fullName);
            return query.getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }
}
