package ru.itmo.tim.responseDto;

import lombok.Getter;
import lombok.Setter;
import ru.itmo.tim.enums.Color;
import ru.itmo.tim.enums.Country;

@Getter
@Setter
public class PersonResponseDto {
    private Long id;
    private Color eyeColor;
    private Color hairColor;
    private LocationResponseDto location;
    private String passportId;
    private Country nationality;
    public PersonResponseDto() {}
}
