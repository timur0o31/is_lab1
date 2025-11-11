package ru.itmo.tim.responseDto;

public class OrganizationResponseDto {
    private Long id;
    private AddressResponseDto officialAddress;
    private Float annualTurnover;
    private Long employeesCount;
    private String fullName;
    private Float rating;
    private AddressResponseDto postalAddress;
    public OrganizationResponseDto() {}

    public AddressResponseDto getOfficialAddress() {
        return officialAddress;
    }

    public void setOfficialAddress(AddressResponseDto officialAddress) {
        this.officialAddress = officialAddress;
    }

    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id = id;
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

    public AddressResponseDto getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(AddressResponseDto postalAddress) {
        this.postalAddress = postalAddress;
    }
}
