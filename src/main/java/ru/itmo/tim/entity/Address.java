package ru.itmo.tim.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Embeddable
@Getter
@Setter
public class Address {

    private String street; //Поле может быть null
    @NotNull
    @Size(min=4)
    @Column(nullable=false)
    private String zipCode; //Длина строки должна быть не меньше 4, Поле не может быть null

    public Address() {
    }
}
