package ru.itmo.tim.responseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationResponseDto {
    private Integer x;
    private Integer y;
    private Long z;
    public LocationResponseDto() {
    }
}
