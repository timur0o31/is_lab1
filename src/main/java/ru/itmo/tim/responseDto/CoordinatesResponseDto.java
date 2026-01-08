package ru.itmo.tim.responseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoordinatesResponseDto {
    private int id;
    private long x;
    private int y;
    public CoordinatesResponseDto() {
    }
}
