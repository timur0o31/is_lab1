package ru.itmo.tim.responseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrganizationResponseDto {
    private Long id;
    private AddressResponseDto officialAddress;
    private Float annualTurnover;
    private Long employeesCount;
    private String fullName;
    private Float rating;
    private AddressResponseDto postalAddress;
    public OrganizationResponseDto() {}
}
