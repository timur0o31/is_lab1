package ru.itmo.tim.requestDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class OrganizationRequestDto {
    @Valid
    @NotNull
    private AddressRequestDto officialAddress;
    @Positive(message="годовой оборот должен быть больше 0")
    private Float annualTurnover;
    @Positive(message="количество работников должно быть больше 0")
    private Long employeesCount;
    @NotNull(message="поле не может быть null")
    @Size(max = 550, message="длина строки не может больше 550")
    private String fullName;
    @NotNull(message="рейтинг не может быть null")
    @Positive(message="рейтинг должен быть положительным")
    private Float rating;
    @Valid
    @NotNull
    private AddressRequestDto postalAddress;

    public OrganizationRequestDto() {
    }

    public AddressRequestDto getOfficialAddress() {
        return officialAddress;
    }

    public void setOfficialAddress(AddressRequestDto officialAddress) {
        this.officialAddress = officialAddress;
    }

    public Float getAnnualTurnover() {
        return annualTurnover;
    }

    public void setAnnualTurnover(Float annualTurnover) {
        this.annualTurnover = annualTurnover;
    }

    public Long getEmployeesCount() {
        return employeesCount;
    }

    public void setEmployeesCount(Long employeesCount) {
        this.employeesCount = employeesCount;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public AddressRequestDto getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(AddressRequestDto postalAddress) {
        this.postalAddress = postalAddress;
    }
}
