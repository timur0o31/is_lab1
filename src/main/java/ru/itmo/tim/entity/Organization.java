package ru.itmo.tim.entity;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "organization")
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

    @Positive(message="годовой оборот должен быть больше 0")
    private Float annualTurnover; //Поле может быть null, Значение поля должно быть больше 0

    @Positive(message="количество работников должно быть больше 0")
    private Long employeesCount; //Поле может быть null, Значение поля должно быть больше 0

    @NotNull
    @Column(nullable = false, length = 550)
    @Size(max = 550, message="длина строки не может больше 550, поле не может быть null")
    private String fullName; //Длина строки не должна быть больше 550, Поле не может быть null

    @NotNull
    @Positive(message="рейтинг не может быть null, должен быть положительным")
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

    public Long getId() {
        return id;
    }

    public Address getOfficialAddress() {
        return officialAddress;
    }

    public void setOfficialAddress(Address officialAddress) {
        this.officialAddress = officialAddress;
    }

    public Float getAnnualTurnover() {
        return annualTurnover;
    }

    public void setAnnualTurnover(Float annualTurnover) {
        this.annualTurnover = annualTurnover;
    }

    public Long getEmployeesCount() {
        return employeesCount;
    }

    public void setEmployeesCount(Long employeesCount) {
        this.employeesCount = employeesCount;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Address getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(Address postalAddress) {
        this.postalAddress = postalAddress;
    }

    public List<Worker> getWorkers() {
        return workers;
    }

    public void setWorkers(List<Worker> workers) {
        this.workers = workers;
    }
}
