package ru.itmo.tim.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Map;

@Provider
public class FieldValidationExceptionMapper implements ExceptionMapper<FieldValidationException> {
    @Override
    public Response toResponse(FieldValidationException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of(
                        "error", "Ошибка данных",
                        "data", e.getMessage()
                ))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}