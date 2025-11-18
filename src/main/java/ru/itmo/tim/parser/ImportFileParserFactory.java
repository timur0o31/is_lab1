package ru.itmo.tim.parser;

import ru.itmo.tim.enums.FileFormat;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class ImportFileParserFactory {
    @Inject
    private List<WorkerImportFileParser> parsers;
    public WorkerImportFileParser getParser(FileFormat format) {
        return parsers.stream().filter(parser -> parser.supports(format))
                .findFirst().orElseThrow(()->new IllegalArgumentException("Неподдерживающий формат файла "));
    }
}
