package ru.itmo.tim.dao;

import ru.itmo.tim.entity.ImportOperation;
import ru.itmo.tim.entity.Organization;
import ru.itmo.tim.utils.BuilderQueryForGetAll;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ImportOperationDao extends GenericDao<ImportOperation>{

    public ImportOperationDao() {
        super(ImportOperation.class);
    }
    public List<ImportOperation> getAllOperations(int page, int size, String sortColumn, boolean sortDirection, Map<String, Object> filters){
        String queryBuilder = BuilderQueryForGetAll.buildQuery("i","ImportOperation",filters,sortColumn,sortDirection);
        EntityManager entityManager = getEntityManager();
        try {
            TypedQuery<ImportOperation> query = entityManager.createQuery(queryBuilder, ImportOperation.class);
            BuilderQueryForGetAll.setQueryParameters(query, filters);
            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);
            return query.getResultList();
        }finally{
            entityManager.close();
        }
    }
    public long countAll(Map<String, Object> filters){
        StringBuilder queryBuilder = new StringBuilder("SELECT COUNT(i) FROM ImportOperation i WHERE 1=1");
        queryBuilder.append(BuilderQueryForGetAll.buildWhereClause(filters, "i"));
        EntityManager entityManager = getEntityManager();
        try {
            TypedQuery<Long> query = entityManager.createQuery(queryBuilder.toString(), Long.class);
            BuilderQueryForGetAll.setQueryParameters(query, filters);
            return query.getSingleResult();
        }finally{
            entityManager.close();
        }
    }
    public List<ImportOperation> getAllOperations(){
        EntityManager entityManager = getEntityManager();
        try {
            TypedQuery<ImportOperation> query = entityManager.createQuery("SELECT i FROM ImportOperation i ", ImportOperation.class);
            return query.getResultList();
        }finally{
            entityManager.close();
        }
    }
}
