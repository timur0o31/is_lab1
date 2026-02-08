package ru.itmo.tim.service;

import ru.itmo.tim.DatabaseInitializier;
import ru.itmo.tim.cache.CacheStatisticsLogging;
import ru.itmo.tim.dao.OrganizationDao;
import ru.itmo.tim.entity.Organization;
import ru.itmo.tim.exception.UniqueViolationException;
import ru.itmo.tim.mapper.OrganizationMapper;
import ru.itmo.tim.requestDto.OrganizationRequestDto;
import ru.itmo.tim.responseDto.OrganizationResponseDto;
import ru.itmo.tim.utils.TxIsolation;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Map;

@CacheStatisticsLogging
@ApplicationScoped
public class OrganizationService {
    @Inject
    private OrganizationDao organizationDao;
    @Inject
    private OrganizationMapper organizationMapper;

    public OrganizationResponseDto createOrganization(OrganizationRequestDto dto) {
        EntityManager em = DatabaseInitializier.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            this.checkConstraint(em,dto.getFullName());
            Organization organization = organizationMapper.toCreateEntity(dto);
            organizationDao.save(em,organization);
            tx.commit();
            return organizationMapper.toResponseDto(organization);
        }catch(Exception e){
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public OrganizationResponseDto updateOrganization(Long  id, OrganizationRequestDto dto) {
        EntityManager em = DatabaseInitializier.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Organization organization = organizationDao.find(em,id);
            if (organization == null) {
                throw new IllegalArgumentException("Organization not found");
            }
            this.checkUpdateUniqueConstraint(em,organization, dto.getFullName());
            organizationMapper.toUpdateEntity(dto, organization);
            organizationDao.update(em, organization);
            tx.commit();
            return organizationMapper.toResponseDto(organization);
        }catch(Exception e){
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Long countWorkers(Long id) {
        return organizationDao.countWorkers(id);
    }

    public void deleteOrganizationWithWorkers(Long id, Long transferToId) {
        EntityManager em = DatabaseInitializier.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Organization organization = organizationDao.find(em,id);
            if (organization == null) {
                throw new IllegalArgumentException("Organization not found");
            }
            if (transferToId != null) {
                Organization newOrganization = organizationDao.find(em,transferToId);
                if (newOrganization == null) {
                    throw new IllegalArgumentException("Organization not found");
                }
                organizationDao.moveWorkersToNewOrganization(em, organization, newOrganization);
            } else {
                long count = organizationDao.countWorkers(em,organization);
                if (count>0) {
                    throw new IllegalArgumentException("Cannot delete organization with workers.");
                }
            }
            organizationDao.delete(em,id);
            tx.commit();
        }catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
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
    public OrganizationResponseDto getOrganizationByFullName(String fullName){
        return organizationMapper.toResponseDto(organizationDao.existByName(fullName));
    }
    public void checkConstraint(EntityManager em,String fullName){
        if (organizationDao.existByName(em,fullName)!=null){
            throw new UniqueViolationException("Организация с данным именем уже существует!");
        }
    }
    public void checkUpdateUniqueConstraint(EntityManager em,Organization organization, String fullName){
        Organization organizationDB = organizationDao.existByName(em,fullName);
        if (organizationDB!=null && organizationDB.getId()!=organization.getId()){
            throw new UniqueViolationException("Организация с данным именем уже существует!");
        }
    }
}
