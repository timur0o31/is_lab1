package ru.itmo.tim.dao;

import ru.itmo.tim.entity.ImportOperation;
import ru.itmo.tim.entity.Organization;
import ru.itmo.tim.utils.BuilderQueryForGetAll;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ImportOperationDao extends GenericDao<ImportOperation>{
    public ImportOperationDao() {
        super(ImportOperation.class);
    }
    public List<ImportOperation> getAllOperations(int page, int size, String sortColumn, boolean sortDirection, Map<String, Object> filters){
        String queryBuilder = BuilderQueryForGetAll.buildQuery("i","Import_operation",filters,sortColumn,sortDirection);
        TypedQuery<ImportOperation> query = entityManager.createQuery(queryBuilder, ImportOperation.class);
        BuilderQueryForGetAll.setQueryParameters(query,filters);
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);
        return query.getResultList();
    }
    public List<ImportOperation> getAllOperations(){
        TypedQuery<ImportOperation> query = entityManager.createQuery("SELECT i FROM ImportOperation i ", ImportOperation.class);
        return query.getResultList();
    }
}
