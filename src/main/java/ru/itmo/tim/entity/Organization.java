package ru.itmo.tim.entity;

import lombok.Getter;
import lombok.Setter;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "organization")
@Getter
@Setter
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "official_street")),
            @AttributeOverride(name = "zipCode", column = @Column(name = "official_zip"))
    })
    private Address officialAddress; //Поле не может быть null

    @Positive
    private Float annualTurnover; //Поле может быть null, Значение поля должно быть больше 0

    @Positive
    private Long employeesCount; //Поле может быть null, Значение поля должно быть больше 0

    @NotNull
    @Column(nullable = false, length = 550)
    @Size(max = 550)
    private String fullName; //Длина строки не должна быть больше 550, Поле не может быть null

    @NotNull
    @Positive
    @Column(nullable = false)
    private Float rating; //Поле не может быть null, Значение поля должно быть больше 0

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "postal_street")),
            @AttributeOverride(name = "zipCode", column = @Column(name = "postal_zip"))
    })
    private Address postalAddress; //Поле не может быть null

    @OneToMany(mappedBy = "organization")
    @JsonbTransient
    private List<Worker> workers;

    public Organization() {
    }
}
