package ru.itmo.tim.parser;

import ru.itmo.tim.enums.FileFormat;
import ru.itmo.tim.requestDto.WorkerRequestDto;

import java.io.InputStream;
import java.util.List;

public interface WorkerImportFileParser {
    //List<WorkerRequestDto> parse(InputStream inputStream);
    boolean supports(FileFormat fileFormat);
}
