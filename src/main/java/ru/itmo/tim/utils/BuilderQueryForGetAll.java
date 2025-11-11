package ru.itmo.tim.utils;

import javax.persistence.TypedQuery;
import java.util.Map;

public class BuilderQueryForGetAll {
    public static  StringBuilder buildWhereClause(Map<String, Object> filters, String alias) {
        StringBuilder query = new StringBuilder();

        for (Map.Entry<String, Object> entry : filters.entrySet()) {
            Object value = entry.getValue();
            if (value != null) {
                if (value instanceof String) {
                    query.append(" AND LOWER(")
                            .append(alias).append(".").append(entry.getKey())
                            .append(") LIKE LOWER(:").append(entry.getKey()).append(")");
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
                    query.setParameter(entry.getKey(), "%" + value + "%");
                } else {
                    query.setParameter(entry.getKey(), value);
                }
            }
        }
    }
}
