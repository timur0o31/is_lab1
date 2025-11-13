package ru.itmo.tim.exception;
import javax.json.bind.JsonbException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Map;
@Provider
public class JsonProcessingExceptionMapper implements ExceptionMapper<ProcessingException> {
    @Override
    public Response toResponse(ProcessingException exception) {
        Throwable cause = exception.getCause();
        if (cause instanceof JsonbException) {
            String message =  cause.getMessage();
            String fieldName = field(message);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Некорректный JSON",
                            "details", "Поле: "+fieldName+ " содержит слишком большое числовое значение"
                    )).build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of("error", "Ошибка обработки запроса", "details", exception.getMessage()))
                .build();
    }
    private String field(String msg) {
        if (msg == null) return "unknown";
        String marker = "property '";
        int last = msg.lastIndexOf(marker);
        if (last < 0) return "unknown";
        last += marker.length();
        int end = msg.indexOf("'", last);
        if (end < 0) return "unknown";
        return msg.substring(last, end);
    }
}
