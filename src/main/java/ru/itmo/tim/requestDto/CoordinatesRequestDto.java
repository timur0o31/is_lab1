package ru.itmo.tim.requestDto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class CoordinatesRequestDto {
    @Max(value = 255,message= "координата x не может быть больше 255")
    private long x;
    @Min(value=-503, message = "координата y не может быть меньше -503")
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
