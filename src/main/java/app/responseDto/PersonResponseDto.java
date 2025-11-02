package app.responseDto;

import app.Color;
import app.Country;

public class PersonResponseDto {
    private Long id;
    private Color eyeColor;
    private Color hairColor;
    private LocationResponseDto location;
    private String passportId;
    private Country nationality;
    public PersonResponseDto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocationResponseDto getLocation() {
        return location;
    }

    public void setLocation(LocationResponseDto location) {
        this.location = location;
    }

    public String getPassportId() {
        return passportId;
    }

    public void setPassportId(String passportId) {
        this.passportId = passportId;
    }

    public Country getNationality() {
        return nationality;
    }

    public void setNationality(Country nationality) {
        this.nationality = nationality;
    }
}
