package ru.itmo.tim.mapper;

import ru.itmo.tim.entity.Organization;
import ru.itmo.tim.requestDto.OrganizationRequestDto;
import ru.itmo.tim.responseDto.OrganizationResponseDto;
import ru.itmo.tim.utils.ParserForFloatValue;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class OrganizationMapper {
    @Inject
    private AddressMapper addressMapper;

    public OrganizationMapper() {
    }

    public OrganizationResponseDto toResponseDto(Organization organization) {
        OrganizationResponseDto dto = new OrganizationResponseDto();
        dto.setId(organization.getId());
        dto.setOfficialAddress(addressMapper.toResponseDto(organization.getOfficialAddress()));
        dto.setAnnualTurnover(organization.getAnnualTurnover());
        dto.setEmployeesCount(organization.getEmployeesCount());
        dto.setFullName(organization.getFullName());
        dto.setRating(organization.getRating());
        dto.setPostalAddress(addressMapper.toResponseDto(organization.getPostalAddress()));
        return dto;
    }

    public Organization toCreateEntity(OrganizationRequestDto organizationRequestDto) {
        Organization organization = new Organization();
        organization.setAnnualTurnover(ParserForFloatValue.safeFloat(organizationRequestDto.getAnnualTurnover(),"Годовой оборот"));
        organization.setEmployeesCount(organizationRequestDto.getEmployeesCount());
        organization.setFullName(organizationRequestDto.getFullName());
        organization.setRating(ParserForFloatValue.safeFloat(organizationRequestDto.getRating(),"Рейтинг"));
        organization.setOfficialAddress(addressMapper.toCreateEntity(organizationRequestDto.getOfficialAddress()));
        organization.setPostalAddress(addressMapper.toCreateEntity(organizationRequestDto.getPostalAddress()));
        return organization;
    }

    public void toUpdateEntity(OrganizationRequestDto organizationRequestDto, Organization organization) {
        if (organizationRequestDto == null || organization == null) {
            return;
        }
        organization.setAnnualTurnover(ParserForFloatValue.safeFloat( organizationRequestDto.getAnnualTurnover(),"Годовой оборот"));
        organization.setEmployeesCount(organizationRequestDto.getEmployeesCount());
        organization.setFullName(organizationRequestDto.getFullName());
        organization.setRating(ParserForFloatValue.safeFloat(organizationRequestDto.getRating(),"Рейтинг"));
        organization.setOfficialAddress(addressMapper.toCreateEntity(organizationRequestDto.getOfficialAddress()));
        organization.setPostalAddress(addressMapper.toCreateEntity(organizationRequestDto.getPostalAddress()));
    }
}
