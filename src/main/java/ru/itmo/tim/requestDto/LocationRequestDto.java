package ru.itmo.tim.requestDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationRequestDto {
    private Integer x;
    private Integer y;
    private Long z;
    public LocationRequestDto(){}
}
