package ru.itmo.tim.parser;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.Produces;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class WorkerImportFileParserListProducer {
    @Inject
    private Instance<WorkerImportFileParser> workerImportFileParsers;

    @Produces
    @ApplicationScoped
    public List<WorkerImportFileParser> getWorkerImportFileParsers() {
        return workerImportFileParsers.stream().collect(Collectors.toList());
    }
}
