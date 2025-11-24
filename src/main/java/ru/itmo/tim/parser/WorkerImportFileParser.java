package ru.itmo.tim.parser;

import ru.itmo.tim.enums.FileFormat;
import ru.itmo.tim.parser.upload.UploadWorker;

import java.io.InputStream;
import java.util.List;

public interface WorkerImportFileParser {
    List<UploadWorker> parse(InputStream inputStream);
    boolean supports(FileFormat fileFormat);
}
