package ru.itmo.tim.parser.upload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UploadLocation {
    private Integer x;
    private Integer y;
    private Long z;
    @Override
    public String toString() {
        return "UploadLocation{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

}
