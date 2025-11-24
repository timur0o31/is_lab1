package ru.itmo.tim.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Embeddable
@Getter
@Setter
public class Coordinates {
    @Max(255)
    private long x; //Максимальное значение поля: 255
    @Min(-503)
    private int y; //Значение поля должно быть больше -504

    public Coordinates() {}
}
