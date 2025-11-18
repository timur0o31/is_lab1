package ru.itmo.tim.parser.raw;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.itmo.tim.enums.Color;
import ru.itmo.tim.enums.Country;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RawPerson {
    private Color eyeColor;
    private Color hairColor;
    private RawLocation location;
    private String passportId;
    private Country nationality;
}
