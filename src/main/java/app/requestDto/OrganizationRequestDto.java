package app.requestDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class OrganizationRequestDto {
    @Valid
    @NotNull
    private AddressRequestDto officialAddress;
    @Positive
    private Float annualTurnover;
    @Positive
    private Long employeesCount;
    @NotNull
    @Size(max = 550)
    private String fullName;
    @NotNull
    @Positive
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
