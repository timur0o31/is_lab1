package ru.itmo.tim.parser.csv;

import ru.itmo.tim.enums.FileFormat;
import ru.itmo.tim.parser.WorkerImportFileParser;
import ru.itmo.tim.parser.upload.UploadWorker;

import javax.enterprise.context.ApplicationScoped;
import java.io.InputStream;
import java.util.List;

@ApplicationScoped
public class CsvImportFileParser implements WorkerImportFileParser {

    @Override
    public boolean supports(FileFormat fileFormat) {
        return FileFormat.CSV.equals(fileFormat);
    }

    @Override
    public List<UploadWorker> parse(InputStream inputStream) {
        throw new UnsupportedOperationException("CSV parsing not implemented yet");
    }
}

