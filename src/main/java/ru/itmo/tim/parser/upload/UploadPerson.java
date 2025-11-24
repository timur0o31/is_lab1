package ru.itmo.tim.parser.upload;

import lombok.Getter;
import lombok.Setter;
import ru.itmo.tim.enums.Color;
import ru.itmo.tim.enums.Country;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
@Getter
@Setter
public class UploadPerson {
    private Color eyeColor;
    private Color hairColor;
    private UploadLocation location;
    @Size(min = 7, message ="длина строки должна быть больше 6")
    @Pattern(regexp=".*\\S.*", message="строка не может быть пустой или состоять только из пробелов")
    private String passportId;
    private Country nationality;
}
