package ru.itmo.tim.dao;

import ru.itmo.tim.utils.BuilderQueryForGetAll;
import ru.itmo.tim.entity.Person;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class PersonDao extends GenericDao<Person> {
    public PersonDao() {
        super(Person.class);
    }

    public List<Person> getAll(int page, int size, String sortColumn, boolean sortDirection, Map<String, Object> filters) {
        String queryBuilder = BuilderQueryForGetAll.buildQuery("p","Person",filters,sortColumn,sortDirection);
        TypedQuery<Person> query = entityManager.createQuery(queryBuilder, Person.class);
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
