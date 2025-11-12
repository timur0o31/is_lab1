package ru.itmo.tim.entity;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Embeddable
public class Coordinates {
    @Max(value = 255,message= "координата x не может быть больше 255")
    private long x; //Максимальное значение поля: 255
    @Min(value=-503, message = "координата y не может быть меньше -503")
    private int y; //Значение поля должно быть больше -504

    public Coordinates() {}

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
