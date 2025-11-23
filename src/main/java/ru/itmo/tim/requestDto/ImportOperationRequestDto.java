package ru.itmo.tim.requestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jboss.resteasy.annotations.providers.multipart.PartType;
import ru.itmo.tim.enums.FileFormat;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImportOperationRequestDto {
    @FormParam("file")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    private InputStream fileStream;
    @FormParam("fileName")
    @PartType(MediaType.TEXT_PLAIN)
    private String fileName;
    @FormParam("format")
    @PartType(MediaType.TEXT_PLAIN)
    private String fileFormat;
}
