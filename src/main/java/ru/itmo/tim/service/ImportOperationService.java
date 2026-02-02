package ru.itmo.tim.service;

import ru.itmo.tim.cache.CacheStatisticsLogging;
import ru.itmo.tim.dao.ImportOperationDao;
import ru.itmo.tim.dao.OrganizationDao;
import ru.itmo.tim.dao.PersonDao;
import ru.itmo.tim.dao.WorkerDao;
import ru.itmo.tim.entity.*;
import ru.itmo.tim.enums.Status;
import ru.itmo.tim.exception.DomainException;
import ru.itmo.tim.exception.UniqueViolationException;
import ru.itmo.tim.mapper.ImportOperationMapper;
import ru.itmo.tim.parser.ImportFileParserFactory;
import ru.itmo.tim.parser.UploadMapper;
import ru.itmo.tim.parser.WorkerImportFileParser;
import ru.itmo.tim.parser.upload.UploadWorker;
import ru.itmo.tim.requestDto.ImportOperationRequestDto;
import ru.itmo.tim.responseDto.ImportOperationResponseDto;
import ru.itmo.tim.utils.TxIsolation;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
@CacheStatisticsLogging
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
    @Inject
    private TxIsolation txIsolation;
    @Inject
    private MinioService minioService;
    @Inject
    private ImportOperationLogService importOperationLogService;
    public ImportOperationService() {}
    @Transactional
    public ImportOperationResponseDto importWorkers(ImportOperationRequestDto dto) throws Exception{
        WorkerImportFileParser parser = parserFactory.getParser();
        List<Worker> ans = new ArrayList<>();
        byte[] fileData;
        String fileKey=null;
        try{
            fileData = dto.getFileStream().readAllBytes();
            try {
                fileKey = minioService.saveFile(
                        new ByteArrayInputStream(fileData),
                        dto.getFileName(),
                        fileData.length
                );
            } catch (Exception e) {
                importOperationLogService.persist(Status.FAILED_INTERNAL,(long) 0,e.getMessage(),fileKey,null);
                throw new RuntimeException("MinIO unable: " + e.getMessage());
            }
            List<UploadWorker> workers = parser.parse(new ByteArrayInputStream(fileData));
            for (UploadWorker upload : workers) {
                Worker worker = uploadMapper.toEntity(upload);
                if (upload.getPerson()!=null){
                    var person = uploadMapper.toEntity(upload.getPerson());
                    if (upload.getPerson().getLocation() != null) {
                        person.setLocation(uploadMapper.toEntity(upload.getPerson().getLocation()));
                    }
                    Person personReference = personDao.existByPassportId(person.getPassportId());
                    if (personReference!=null){
                        String errorMessage = "Нарушение ограничения уникальности по passportId. Для worker c name: "+worker.getName()+" нельзя создать person с таким же passportId:"+upload.getPerson().getPassportId();
                        throw new UniqueViolationException(errorMessage); //isSamePerson(person,personReference);
                    }else {
                        personDao.save(person);
                        worker.setPerson(person);
                    }
                }
                if (upload.getOrganization()!=null){
                    var organization = uploadMapper.toEntity(upload.getOrganization());
                    Organization organizationReference = organizationDao.existByName(upload.getOrganization().getFullName());
                    if (organizationReference!=null){
                        String errorMessage = "Нарушение ограничения уникальности по fullName. Для worker с name:"+worker.getName()+"нельзя создать organization с таким же fullName:"+upload.getOrganization().getFullName();
                        throw new UniqueViolationException(errorMessage); //isSameOrganization(organization, organizationReference);
                    }
                    else{
                        if (upload.getOrganization().getOfficialAddress() != null) {
                            organization.setOfficialAddress(uploadMapper.toEntity(upload.getOrganization().getOfficialAddress()));
                        }
                        if (upload.getOrganization().getPostalAddress() != null) {
                            organization.setPostalAddress(uploadMapper.toEntity(upload.getOrganization().getPostalAddress()));
                        }
                        organizationDao.save(organization);
                        worker.setOrganization(organization);
                    }
                }
                if (worker.getStartDate()!=null && worker.getEndDate()!=null) {
                    if (worker.getEndDate().isBefore(worker.getStartDate().toLocalDate())) throw new DomainException(worker.getName() + ": дата окончания работы не может быть раньше трудоустройства");
                }
                ans.add(worker);
                workerDao.save(worker);
            }
        }catch(Exception e){
            importOperationLogService.persist(Status.FAILED, (long) 0, e.getMessage(), fileKey, dto.getFileName());
            throw e;
        }
        return importOperationLogService.persist(Status.SUCCESS,(long) ans.size(),"",fileKey,dto.getFileName());
    }
    public List<ImportOperationResponseDto>getAllImportOperations(){
        return importOperationDao.getAllOperations().stream()
                .map(importOperationMapper::toResponseDto).toList();
    }
    public List<ImportOperationResponseDto> getAllImportOperations(int page, int size, String sortColumn, boolean asc, Map<String, Object> filters){
        return importOperationDao.getAllOperations(page,size,sortColumn,asc, filters).stream()
                .map(importOperationMapper::toResponseDto).toList();
    }
    public Long getCount(Map<String,Object> filters){
        return importOperationDao.countAll(filters);
    }
    public void isSamePerson(Person person, Person personreference){
        if (!samePerson(person,personreference)) throw new DomainException("Person с passportId=" + person.getPassportId()
                + " уже существует с другими полями");
    }
    public void isSameOrganization(Organization organization, Organization organizationReference){
        if (!sameOrganization(organization, organizationReference)){
            throw new DomainException("Organization с fullName=" + organization.getFullName()
            + " уже существует с другими полями");
        }
    }
    private boolean samePerson(Person person, Person person1){
        return Objects.equals(person.getNationality(), person1.getNationality())
                && Objects.equals(person.getEyeColor(), person1.getEyeColor())
                && Objects.equals(person.getHairColor(), person1.getHairColor())
                && sameLocation(person.getLocation(), person1.getLocation());
    }
    private boolean sameOrganization(Organization left, Organization right) {
        return Objects.equals(left.getAnnualTurnover(), right.getAnnualTurnover())
                && Objects.equals(left.getEmployeesCount(), right.getEmployeesCount())
                && Objects.equals(left.getRating(), right.getRating())
                && sameAddress(left.getOfficialAddress(), right.getOfficialAddress())
                && sameAddress(left.getPostalAddress(), right.getPostalAddress());
    }
    private boolean sameLocation(Location left, Location right) {
        if (left == null && right == null) {
            return true;
        }
        if (left == null || right == null) {
            return false;
        }
        return Objects.equals(left.getX(), right.getX())
                && Objects.equals(left.getY(), right.getY())
                && Objects.equals(left.getZ(), right.getZ());
    }
    private boolean sameAddress(Address left, Address right) {
        if (left == null && right == null) {
            return true;
        }
        if (left == null || right == null) {
            return false;
        }
        return Objects.equals(left.getStreet(), right.getStreet())
                && Objects.equals(left.getZipCode(), right.getZipCode());
    }
    public ImportOperationResponseDto getImportOperationById(Long id){
        return importOperationMapper.toResponseDto(importOperationDao.find(id));
    }
    public InputStream downloadImportFile(Long importId) throws Exception{
        ImportOperation operation = importOperationDao.find(importId);
        if (operation == null){
            throw new RuntimeException("Import operation not found");
        }
        if (operation.getFileKey() == null){
            throw new RuntimeException("File not found for this operation");
        }
        try{
            return minioService.getFile(operation.getFileKey());
        }catch(Exception e){
            throw new RuntimeException("MinIO storage unavailable " + e.getMessage());
        }
    }
}
