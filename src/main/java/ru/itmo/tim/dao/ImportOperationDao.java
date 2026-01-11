package ru.itmo.tim.dao;

import ru.itmo.tim.entity.ImportOperation;
import ru.itmo.tim.entity.Organization;
import ru.itmo.tim.utils.BuilderQueryForGetAll;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import javax.transaction.UserTransaction;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ImportOperationDao extends GenericDao<ImportOperation>{
    @Inject
    private UserTransaction userTransaction;
    public ImportOperationDao() {
        super(ImportOperation.class);
    }
    public List<ImportOperation> getAllOperations(int page, int size, String sortColumn, boolean sortDirection, Map<String, Object> filters){
        String queryBuilder = BuilderQueryForGetAll.buildQuery("i","ImportOperation",filters,sortColumn,sortDirection);
        TypedQuery<ImportOperation> query = entityManager.createQuery(queryBuilder, ImportOperation.class);
        BuilderQueryForGetAll.setQueryParameters(query,filters);
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);
        return query.getResultList();
    }
    public long countAll(Map<String, Object> filters){
        StringBuilder queryBuilder = new StringBuilder("SELECT COUNT(i) FROM ImportOperation i WHERE 1=1");
        queryBuilder.append(BuilderQueryForGetAll.buildWhereClause(filters, "i"));
        TypedQuery<Long> query = entityManager.createQuery(queryBuilder.toString(), Long.class);
        BuilderQueryForGetAll.setQueryParameters(query,filters);
        return query.getSingleResult();
    }
    public List<ImportOperation> getAllOperations(){
        TypedQuery<ImportOperation> query = entityManager.createQuery("SELECT i FROM ImportOperation i ", ImportOperation.class);
        return query.getResultList();
    }
}
