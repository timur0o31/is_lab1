package ru.itmo.tim.exception;
import javax.json.bind.JsonbException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                            "data", "Поле: "+fieldName+ " содержит слишком большое числовое значение"
                    )).build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of("error", "Ошибка обработки запроса", "details", exception.getMessage()))
                .build();
    }
    private String field(String msg) {
        if (msg == null) return "unknown";
        String marker = "property '";
        int pos = msg.lastIndexOf(marker);
        if (pos >= 0) {
            int start = pos + marker.length();
            int end = msg.indexOf("'", start);
            if (end > start) return msg.substring(start, end);
        }

        Pattern p = Pattern.compile("'(\\w+)'");
        Matcher m = p.matcher(msg);
        if (m.find()) return m.group(1);
        return "unknown";
    }
}
