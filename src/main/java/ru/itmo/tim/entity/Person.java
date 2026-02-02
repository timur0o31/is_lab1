package ru.itmo.tim.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.itmo.tim.enums.Color;
import ru.itmo.tim.enums.Country;

import javax.persistence.*;
import org.hibernate.annotations.Cache;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "person")
@Getter
@Setter
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Color eyeColor; //Поле может быть null

    @Enumerated(EnumType.STRING)
    private Color hairColor; //Поле может быть null

    @Embedded
    private Location location; //Поле может быть null


    @Size(min = 7)
    private String passportId; //Строка не может быть пустой, Длина строки должна быть не меньше 7, Поле может быть null

    @Enumerated(EnumType.STRING)
    private Country nationality; //Поле может быть null
    @OneToMany(mappedBy="person")
    private List<Worker> worker;

    public Person() {
    }
}
