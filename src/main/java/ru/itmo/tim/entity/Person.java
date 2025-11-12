package ru.itmo.tim.entity;

import ru.itmo.tim.enums.Color;
import ru.itmo.tim.enums.Country;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Color eyeColor; //Поле может быть null

    @Enumerated(EnumType.STRING)
    private Color hairColor; //Поле может быть null

    @Embedded
    private Location location; //Поле может быть null


    @Size(min = 7, message ="строка не может быть пустой, длина строки должна быть больше 6")
    private String passportId; //Строка не может быть пустой, Длина строки должна быть не меньше 7, Поле может быть null

    @Enumerated(EnumType.STRING)
    private Country nationality; //Поле может быть null

    public Person() {
    }

    public Long getId() {
        return id;
    }

    public Color getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(Color eyeColor) {
        this.eyeColor = eyeColor;
    }

    public Color getHairColor() {
        return hairColor;
    }

    public void setHairColor(Color hairColor) {
        this.hairColor = hairColor;
    }

    public @NotNull Location getLocation() {
        return location;
    }

    public void setLocation(@NotNull Location location) {
        this.location = location;
    }

    public String getPassportId() {
        return passportId;
    }

    public void setPassportId(String passportID) {
        this.passportId = passportID;
    }

    public Country getNationality() {
        return nationality;
    }

    public void setNationality(Country nationality) {
        this.nationality = nationality;
    }

}
