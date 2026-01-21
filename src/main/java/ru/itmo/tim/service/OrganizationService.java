package ru.itmo.tim.service;

import ru.itmo.tim.dao.OrganizationDao;
import ru.itmo.tim.entity.Organization;
import ru.itmo.tim.entity.Person;
import ru.itmo.tim.exception.DomainException;
import ru.itmo.tim.exception.UniqueViolationException;
import ru.itmo.tim.mapper.OrganizationMapper;
import ru.itmo.tim.parser.UploadMapper;
import ru.itmo.tim.parser.upload.UploadOrganization;
import ru.itmo.tim.parser.upload.UploadWorker;
import ru.itmo.tim.requestDto.OrganizationRequestDto;
import ru.itmo.tim.responseDto.OrganizationResponseDto;
import ru.itmo.tim.utils.TxIsolation;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class OrganizationService {
    @Inject
    private OrganizationDao organizationDao;
    @Inject
    private OrganizationMapper organizationMapper;
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public OrganizationResponseDto createOrganization(OrganizationRequestDto dto) {
        txIsolation.setLocalRepeatableRead();
        Organization organization = organizationMapper.toCreateEntity(dto);
        this.checkConstraint(organization);
        organizationDao.save(organization);
        return organizationMapper.toResponseDto(organization);
    }
    @Inject
    private UploadMapper uploadMapper;

    @Inject
    private TxIsolation txIsolation;
    @Transactional
    public OrganizationResponseDto updateOrganization(Long  id, OrganizationRequestDto dto) {
        txIsolation.setLocalRepeatableRead();
        Organization organization = organizationDao.find(id);
        if (organization == null) {
            throw new IllegalArgumentException("Organization not found");
        }
        this.checkUpdateUniqueConstraint(organization,dto.getFullName());
        organizationMapper.toUpdateEntity(dto, organization);
        organizationDao.update(organization);
        return organizationMapper.toResponseDto(organization);
    }

    public Long countWorkers(Long id) {
        return organizationDao.countWorkers(id);
    }
    @Transactional
    public void deleteOrganizationWithWorkers(Long id, Long transferToId) {
        txIsolation.setLocalRepeatableRead();
        Organization organization = organizationDao.find(id);
        if (organization == null) {
            throw new IllegalArgumentException("Organization not found");
        }
        if (transferToId != null) {
            Organization newOrganization = organizationDao.find(transferToId);
            if (newOrganization == null) {
                throw new IllegalArgumentException("Organization not found");
            }
            organization.getWorkers().forEach(worker -> worker.setOrganization(newOrganization));
        }else{
            if(!organization.getWorkers().isEmpty()){
                throw new IllegalArgumentException("Cannot delete organization with workers.");
            }
        }
        organizationDao.delete(organization);
    }
    public OrganizationResponseDto getOrganization(Long id) {
        return organizationMapper.toResponseDto(this.getOrganizationEntity(id));
    }
    public Organization getOrganizationEntity(Long id){
        Organization organization = organizationDao.find(id);
        if (organization == null) {
            throw new IllegalArgumentException("Organization not found");
        }
        return organization;
    }

    public List<OrganizationResponseDto> getAllOrganizations(int page, int size, String sortColumn, boolean sortDirection, Map<String, Object> filters){
        return organizationDao.getAll(page, size, sortColumn, sortDirection, filters).stream()
                .map(organizationMapper::toResponseDto)
                .toList();
    }
    public Long getCount(Map<String, Object> filters){
        return organizationDao.countAll(filters);
    }

    public List<OrganizationResponseDto> getOtherOrganizations(Long id){
        return organizationDao.findOtherOrganizations(id).stream()
                .map(organizationMapper::toResponseDto)
                .toList();
    }
    @Transactional
    public OrganizationResponseDto getOrganizationByFullName(String fullName){
        return organizationMapper.toResponseDto(organizationDao.existByName(fullName));
    }
    public void checkConstraint(Organization organization){
        if (organizationDao.existByName(organization.getFullName())!=null){
            throw new UniqueViolationException("Организация с данным именем уже существует!");
        }
    }
    public void checkUpdateUniqueConstraint(Organization organization, String fullName){
        Organization organizationDB = organizationDao.existByName(fullName);
        if (organizationDB!=null && organizationDB.getId()!=organization.getId()){
            throw new UniqueViolationException("Организация с данным именем уже существует!");
        }
    }
}
