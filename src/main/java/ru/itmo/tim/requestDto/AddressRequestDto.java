package ru.itmo.tim.requestDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AddressRequestDto {

    private String street;

    @Size(min = 4, message="длина почтового индекса должна быть больше 3")
    @NotNull(message="Поле не может быть null")
    private String zipCode;

    public AddressRequestDto() {}

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
