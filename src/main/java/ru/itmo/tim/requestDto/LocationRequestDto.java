package ru.itmo.tim.requestDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationRequestDto {
    private Integer x;
    private Integer y;
    private Long z;
    public LocationRequestDto(){

    }


    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Long getZ() {
        return z;
    }

    public void setZ(Long z) {
        this.z = z;
    }
}
