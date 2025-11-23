package ru.itmo.tim.dao;

import ru.itmo.tim.utils.BuilderQueryForGetAll;
import ru.itmo.tim.entity.Organization;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class OrganizationDao extends GenericDao<Organization> {
    public OrganizationDao() {
        super(Organization.class);
    }
    public long countWorkers(Long orgId){
        return entityManager.createQuery(
                "SELECT COUNT(w) FROM Worker w WHERE w.organization.id = :id", Long.class)
                .setParameter("id", orgId)
                .getSingleResult();
    }
    public List<Organization> findOtherOrganizations(Long excludeId) {
        return entityManager.createQuery("SELECT o FROM Organization o WHERE o.id <> :excludeId", Organization.class)
                .setParameter("excludeId", excludeId)
                .getResultList();
    }
    public List<Organization> getAll(int page, int size, String sortColumn, boolean sortDirection, Map<String, Object> filters) {
        String queryBuilder = BuilderQueryForGetAll.buildQuery("o","Organization",filters,sortColumn,sortDirection);
        TypedQuery<Organization> query = entityManager.createQuery(queryBuilder, Organization.class);
        BuilderQueryForGetAll.setQueryParameters(query,filters);
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);
        return query.getResultList();
    }
    public long countAll(Map<String, Object> filters) {
        StringBuilder queryBuilder = new StringBuilder("SELECT COUNT(org) FROM Organization org WHERE 1=1");
        queryBuilder.append(BuilderQueryForGetAll.buildWhereClause(filters, "org"));
        TypedQuery<Long> query = entityManager.createQuery(queryBuilder.toString(), Long.class);
        BuilderQueryForGetAll.setQueryParameters(query,filters);
        return query.getSingleResult();
    }
}
