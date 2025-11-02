package app.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Embeddable
public class Address {

    private String street; //Поле может быть null
    @NotNull
    @Size(min=4)
    @Column(nullable=false)
    private String zipCode; //Длина строки должна быть не меньше 4, Поле не может быть null

    public Address() {
    }

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
