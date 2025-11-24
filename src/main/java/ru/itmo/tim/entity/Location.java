package ru.itmo.tim.entity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Embeddable
@Setter
@Getter
public class Location {
    private Integer x;
    private Integer y;
    private Long z;
    public Location() {
    }
}
