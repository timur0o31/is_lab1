package ru.itmo.tim.service;

import ru.itmo.tim.dao.ImportOperationDao;
import ru.itmo.tim.dao.OrganizationDao;
import ru.itmo.tim.dao.PersonDao;
import ru.itmo.tim.dao.WorkerDao;
import ru.itmo.tim.entity.ImportOperation;
import ru.itmo.tim.entity.Worker;
import ru.itmo.tim.enums.Status;
import ru.itmo.tim.mapper.ImportOperationMapper;
import ru.itmo.tim.parser.ImportFileParserFactory;
import ru.itmo.tim.parser.UploadMapper;
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
    private WorkerDao workerDao;
    @Inject
    private PersonDao personDao;
    @Inject
    private OrganizationDao organizationDao;
    @Inject
    private UploadMapper uploadMapper;
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
            for (UploadWorker upload : workers) {
                Worker worker = uploadMapper.toEntity(upload);
                if (upload.getPerson()!=null){
                    var person = uploadMapper.toEntity(upload.getPerson());
                    if (upload.getPerson().getLocation() != null) {
                        person.setLocation(uploadMapper.toEntity(upload.getPerson().getLocation()));
                    }
                    personDao.save(person);
                    worker.setPerson(person);
                }
                if (upload.getOrganization()!=null){
                    var organization = uploadMapper.toEntity(upload.getOrganization());
                    System.out.println(organization.getRating());
                    System.out.println(organization.getFullName());
                    if (upload.getOrganization().getOfficialAddress() != null) {
                        organization.setOfficialAddress(uploadMapper.toEntity(upload.getOrganization().getOfficialAddress()));
                    }

                    if (upload.getOrganization().getPostalAddress() != null) {
                        organization.setPostalAddress(uploadMapper.toEntity(upload.getOrganization().getPostalAddress()));
                    }
                    organizationDao.save(organization);
                    worker.setOrganization(organization);
                }
                workerDao.save(worker);
                count++;
            }
            importOperation.setStatus(Status.ACCEPT);
            importOperation.setCount(count);
        }catch(Exception e){
            e.printStackTrace();
            importOperation.setStatus(Status.REJECT);
        }
        importOperationDao.save(importOperation);
        return importOperationMapper.toResponseDto(importOperation);
    }
}
