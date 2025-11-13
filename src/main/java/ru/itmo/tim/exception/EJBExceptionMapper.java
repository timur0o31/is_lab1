package ru.itmo.tim.exception;
import javax.ejb.EJBException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Map;

@Provider
public class EJBExceptionMapper implements ExceptionMapper<EJBException> {

    @Override
    public Response toResponse(EJBException e) {

        Throwable cause = e.getCause();
        while (cause != null) {
            if (cause instanceof DomainException domain) {
                return Response.status(400)
                        .entity(Map.of("error", domain.getMessage()))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }
            cause = cause.getCause();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(Map.of("error", "Internal server error")).type(MediaType.APPLICATION_JSON)
                .build();
    }
}
