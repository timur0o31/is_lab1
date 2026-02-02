package ru.itmo.tim.dao;

import ru.itmo.tim.utils.BuilderQueryForGetAll;
import ru.itmo.tim.entity.Person;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class PersonDao extends GenericDao<Person> {
    public PersonDao() {
        super(Person.class);
    }

    public List<Person> getAll(int page, int size, String sortColumn, boolean sortDirection, Map<String, Object> filters) {
        var entityManager = getEntityManager();
        try{
            String queryBuilder = BuilderQueryForGetAll.buildQuery("p","Person",filters,sortColumn,sortDirection);
            TypedQuery<Person> query = entityManager.createQuery(queryBuilder, Person.class);
            BuilderQueryForGetAll.setQueryParameters(query,filters);
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
            StringBuilder queryBuilder = new StringBuilder("SELECT COUNT(p) FROM Person p WHERE 1=1");
            queryBuilder.append(BuilderQueryForGetAll.buildWhereClause(filters, "p"));
            TypedQuery<Long> query = entityManager.createQuery(queryBuilder.toString(), Long.class);
            BuilderQueryForGetAll.setQueryParameters(query, filters);
            return query.getSingleResult();
        }finally{
            entityManager.close();
        }
    }
    public boolean hasWorkers(long personId){
        var entityManager = getEntityManager();
        try {
            Long count = entityManager.createQuery(
                            "SELECT COUNT(w) FROM Worker w WHERE w.person.id=:pid", Long.class)
                    .setParameter("pid", personId).getSingleResult();
            return count > 0;
        } finally {
            entityManager.close();
        }
    }
    public Person existByPassportId(String passportId){
        var entityManager = getEntityManager();
        try {
            TypedQuery<Person> query = entityManager.createQuery("SELECT p FROM Person p WHERE p.passportId= :passportId", Person.class)
                    .setParameter("passportId", passportId);
            return query.getSingleResult();
        }catch(NoResultException e){
            return null;
        } finally {
            entityManager.close();
        }
    }
}
