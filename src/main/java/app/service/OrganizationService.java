package app.service;

import app.dao.OrganizationDao;
import app.entities.Organization;
import app.mappers.OrganizationMapper;
import app.requestDto.OrganizationRequestDto;
import app.responseDto.OrganizationResponseDto;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Stateless
public class OrganizationService {
    @Inject
    private OrganizationDao organizationDao;
    @Inject
    private OrganizationMapper organizationMapper;

    public OrganizationResponseDto createOrganization(OrganizationRequestDto dto) {
        Organization organization = organizationMapper.toCreateEntity(dto);
        organizationDao.save(organization);
        return organizationMapper.toResponseDto(organization);
    }
    public OrganizationResponseDto updateOrganization(Long  id, OrganizationRequestDto dto) {
        Organization organization = organizationDao.find(id);
        if (organization == null) {
            throw new IllegalArgumentException("Organization not found");
        }
        organizationMapper.toUpdateEntity(dto, organization);
        organizationDao.update(organization);
        return organizationMapper.toResponseDto(organization);
    }
    public Long countWorkers(Long id) {
        return organizationDao.countWorkers(id);
    }
    public void deleteOrganizationWithWorkers(Long id, Long transferToId) {
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
}
