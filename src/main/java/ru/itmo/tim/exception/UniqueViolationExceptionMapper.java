package ru.itmo.tim.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Map;

@Provider
public class UniqueViolationExceptionMapper implements ExceptionMapper<UniqueViolationException> {
    @Override
    public Response toResponse(UniqueViolationException e) {
        return Response.status(Response.Status.CONFLICT)
                .entity(Map.of(
                        "error", "Ошибка ограничений уникальности",
                        "data", e.getMessage()
                ))
                .build();
    }

}
