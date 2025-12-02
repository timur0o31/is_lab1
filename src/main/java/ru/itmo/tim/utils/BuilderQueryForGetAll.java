package ru.itmo.tim.utils;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;

public class BuilderQueryForGetAll {

    public static String buildQuery(String alias, String entityName, Map<String,Object> filters,
                                    String sortColumn, boolean asc){
        StringBuilder query = new StringBuilder("SELECT ");
        query.append(alias).append(" FROM ").append(entityName)
                .append(" ").append(alias).append(" WHERE 1=1 ");
        query.append(buildWhereClause(filters,alias));
        if (sortColumn != null && !sortColumn.isEmpty()){
            query.append(" ORDER BY ").append(alias).append(".").append(sortColumn)
                    .append(asc ? " ASC" : " DESC");
        }
        return query.toString();
    }

    public static  StringBuilder buildWhereClause(Map<String, Object> filters, String alias) {
        StringBuilder query = new StringBuilder();
        for (Map.Entry<String, Object> entry : filters.entrySet()) {
            Object value = entry.getValue();
            if (value != null) {
                if (value instanceof String) {
                    query.append(" AND ")
                            .append(alias).append(".").append(entry.getKey())
                            .append(" = :").append(entry.getKey());
                } else {
                    query.append(" AND ")
                            .append(alias).append(".").append(entry.getKey())
                            .append(" = :").append(entry.getKey());
                }
            }
        }
        return query;
    }
    public static void setQueryParameters(TypedQuery<?> query, Map<String, Object> filters) {
        for (Map.Entry<String, Object> entry : filters.entrySet()) {
            Object value = entry.getValue();
            if (value != null) {
                if (value instanceof String) {
                    query.setParameter(entry.getKey(), value ); //
                } else {
                    query.setParameter(entry.getKey(), value);
                }
            }
        }
    }
}
