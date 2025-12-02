package ru.itmo.tim.parser;

import ru.itmo.tim.enums.FileFormat;
import ru.itmo.tim.parser.csv.CsvImportFileParser;
import ru.itmo.tim.parser.json.JsonImportFileParser;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class ImportFileParserFactory {
    @Inject
    JsonImportFileParser jsonImportFileParser;
    @Inject
    CsvImportFileParser csvImportFileParser;
    public WorkerImportFileParser getParser(FileFormat format) {
        switch (format) {
            case JSON:
                return jsonImportFileParser;
            case CSV:
                return csvImportFileParser;
            default:
                throw new IllegalArgumentException("Неподдерживаемый формат: " + format);
        }
    }
}
