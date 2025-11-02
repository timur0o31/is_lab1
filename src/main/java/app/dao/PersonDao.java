package app.dao;

import app.BuilderQueryForGetAll;
import app.entities.Person;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;

@Stateless
public class PersonDao extends GenericDao<Person> {
    public PersonDao() {
        super(Person.class);
    }

    public List<Person> getAll(int page, int size, String sortColumn, boolean sortDirection, Map<String, Object> filters) {
        StringBuilder queryBuilder = new StringBuilder("SELECT p FROM Person p WHERE 1=1");
        queryBuilder.append(BuilderQueryForGetAll.buildWhereClause(filters, "p"));
        if (sortColumn != null && !sortColumn.isEmpty()) {
            queryBuilder.append(" ORDER BY p.").append(sortColumn);
            queryBuilder.append(sortDirection ? " ASC" : " DESC");
        }
        TypedQuery<Person> query = entityManager.createQuery(queryBuilder.toString(), Person.class);
        BuilderQueryForGetAll.setQueryParameters(query,filters);
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);

        return query.getResultList();
    }
    public long countAll(Map<String, Object> filters) {
        StringBuilder queryBuilder = new StringBuilder("SELECT COUNT(p) FROM Person p WHERE 1=1");
        queryBuilder.append(BuilderQueryForGetAll.buildWhereClause(filters, "p"));
        TypedQuery<Long> query = entityManager.createQuery(queryBuilder.toString(), Long.class);
        BuilderQueryForGetAll.setQueryParameters(query,filters);
        return query.getSingleResult();
    }

}
