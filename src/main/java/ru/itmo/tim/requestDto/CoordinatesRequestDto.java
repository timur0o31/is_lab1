package ru.itmo.tim.requestDto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class CoordinatesRequestDto {
    @Max(255)
    private long x;
    @Min(-503)
    private int y;
    public CoordinatesRequestDto() {

    }

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
