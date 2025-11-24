package ru.itmo.tim.service;

import ru.itmo.tim.dao.ImportOperationDao;
import ru.itmo.tim.entity.ImportOperation;
import ru.itmo.tim.entity.Worker;
import ru.itmo.tim.enums.Status;
import ru.itmo.tim.mapper.ImportOperationMapper;
import ru.itmo.tim.parser.ImportFileParserFactory;
import ru.itmo.tim.parser.WorkerImportFileParser;
import ru.itmo.tim.parser.upload.UploadWorker;
import ru.itmo.tim.requestDto.ImportOperationRequestDto;
import ru.itmo.tim.requestDto.WorkerRequestDto;
import ru.itmo.tim.responseDto.ImportOperationResponseDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@ApplicationScoped
public class ImportOperationService {
    @Inject
    private ImportOperationDao importOperationDao;
    @Inject
    private WorkerService workerService;
    @Inject
    private ImportOperationMapper importOperationMapper;
    @Inject
    private ImportFileParserFactory parserFactory;
    public ImportOperationService() {}
    public ImportOperationResponseDto importWorkers(ImportOperationRequestDto dto){
        WorkerImportFileParser parser = parserFactory.getParser(dto.getFileFormat());
        List<UploadWorker> workers = parser.parse(dto.getFileStream());
        ImportOperation importOperation = new ImportOperation();
        long count = 0;
        try{
            for (UploadWorker worker : workers) {
                workerService.createWorkerFromImport(worker);
                count++;
            }
            importOperation.setStatus(Status.ACCEPT);
            importOperation.setCount(count);
        }catch(Exception e){
            importOperation.setStatus(Status.REJECT);
        }
        importOperationDao.save(importOperation);
        return importOperationMapper.toResponseDto(importOperation);
    }
}
