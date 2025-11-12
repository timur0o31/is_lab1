package ru.itmo.tim.exception;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.LinkedHashMap;
import java.util.Map;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException e) {
        Map<String, String> fields = new LinkedHashMap<>();

        e.getConstraintViolations().forEach(v -> {
            String path = v.getPropertyPath().toString();
            String field = path.substring(path.lastIndexOf('.') + 1);
            fields.put(field, v.getMessage());
        });

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of(
                        "error", "Невалидные данные",
                        "fields", fields
                ))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
