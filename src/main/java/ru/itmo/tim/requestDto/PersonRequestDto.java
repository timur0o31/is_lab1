package ru.itmo.tim.requestDto;

import ru.itmo.tim.enums.Color;
import ru.itmo.tim.enums.Country;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PersonRequestDto {
    private Color eyeColor;
    private Color hairColor;
    private LocationRequestDto location;
    @NotNull
    @Size(min = 7)
    private String passportId;
    private Country nationality;

    public PersonRequestDto() {
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

    public LocationRequestDto getLocation() {
        return location;
    }

    public void setLocation(LocationRequestDto location) {
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
