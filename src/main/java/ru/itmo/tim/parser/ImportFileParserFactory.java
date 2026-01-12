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

    public WorkerImportFileParser getParser() {
        return jsonImportFileParser;
    }
}
