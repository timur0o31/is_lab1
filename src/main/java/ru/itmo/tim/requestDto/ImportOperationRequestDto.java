package ru.itmo.tim.requestDto;
import ru.itmo.tim.enums.FileFormat;
import java.io.InputStream;

public class ImportOperationRequestDto {
    private InputStream fileStream;
    private String fileName;
    private FileFormat fileFormat;
}
