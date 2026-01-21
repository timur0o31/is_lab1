package ru.itmo.tim.service;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    public ImportOperationService() {}
    @Transactional
    public List<Worker> importWorkers(ImportOperationRequestDto dto){
        txIsolation.setLocalSerializable();
        WorkerImportFileParser parser = parserFactory.getParser();
        List<Worker> ans = new ArrayList<>();
        String messageError = "";
        try{
            List<UploadWorker> workers = parser.parse(dto.getFileStream());
            for (UploadWorker upload : workers) {
                Worker worker = uploadMapper.toEntity(upload);
                if (upload.getPerson()!=null){
                    var person = uploadMapper.toEntity(upload.getPerson());
                    if (upload.getPerson().getLocation() != null) {
                        person.setLocation(uploadMapper.toEntity(upload.getPerson().getLocation()));
                    }
                    Person personReference = personDao.existByPassportId(person.getPassportId());
                    if (personReference!=null){
                        throw new UniqueViolationException("Нарушение ограничения уникальности по passportId. Для worker c name: "+worker.getName()+" нельзя создать person с таким же passportId:"+upload.getPerson().getPassportId()); //isSamePerson(person,personReference);
                    }else {
                        personDao.save(person);
                        worker.setPerson(person);
                    }
                }
                if (upload.getOrganization()!=null){
                    var organization = uploadMapper.toEntity(upload.getOrganization());
                    Organization organizationReference = organizationDao.existByName(upload.getOrganization().getFullName());
                    if (organizationReference!=null){
                        throw new UniqueViolationException("Нарушение ограничения уникальности по fullName. Для worker с name:"+worker.getName()+"нельзя создать organization с таким же fullName:"+upload.getOrganization().getFullName()); //isSameOrganization(organization, organizationReference);
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
            throw e;
        }
        return ans;
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
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public ImportOperationResponseDto persist(Status status, Long count, String message){
        ImportOperation importOperation = new ImportOperation();
        importOperation.setCount(count);
        importOperation.setStatus(status);
        if (!message.isEmpty()) importOperation.setMessage(message);
        importOperationDao.save(importOperation);
        return importOperationMapper.toResponseDto(importOperation);
    }
}
