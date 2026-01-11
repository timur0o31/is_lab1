package ru.itmo.tim.parser.upload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UploadCoordinates {
    @Max(value = 255,message= "координата x не может быть больше 255")
    private long x;
    @Min(value=-503, message = "координата y не может быть меньше -503")
    private int y;
    @Override
    public String toString() {
        return "UploadCoordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

}
