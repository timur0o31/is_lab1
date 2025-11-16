package ru.itmo.tim.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Map;

@Provider
public class DomainExceptionMapper implements ExceptionMapper<DomainException> {

    @Override
    public Response toResponse(DomainException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of("error","Ошибка доменной логики","data", e.getMessage()))
                .build();
    }
}

