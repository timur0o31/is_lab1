package ru.itmo.tim.parser;

import ru.itmo.tim.parser.json.JsonImportFileParser;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ImportFileParserFactory {
    @Inject
    JsonImportFileParser jsonImportFileParser;

    public WorkerImportFileParser getParser() {
        return jsonImportFileParser;
    }
}
