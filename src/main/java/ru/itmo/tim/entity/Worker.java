package ru.itmo.tim.entity;

import ru.itmo.tim.enums.Position;
import org.hibernate.annotations.CreationTimestamp;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "worker")
public class Worker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

    @Column(nullable = false)
    @NotBlank(message="Поле для имени должно быть заполнено")
    private String name; //Поле не может быть null, Строка не может быть пустой

    @NotNull
    @Embedded
    private Coordinates coordinates; //Поле не может быть null

    @CreationTimestamp
    @Column(name="creation_date")
    private LocalDate creationDate;  //Поле не может быть null, Значение этого поля должно генерироваться автоматически

    @NotNull(message="Организация должна быть указана, не может быть null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonbTransient
    @JoinColumn(name="organization_id", nullable = false)
    private Organization organization;

    @NotNull(message="Зарплата должна быть больше 0")
    @Positive
    private Float salary;

    @Positive(message="Рейтинг должен быть положителен")
    private int rating;

    @NotNull(message="Дата начала работы не может быть null")
    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Position position;

    @NotNull(message="Персона должна быть указана")
    @OneToOne
    @JoinColumn(name = "person_id", unique=true, nullable = false)
    private Person person;
    public Worker() {}

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public @NotNull Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(@NotNull Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public @NotNull Organization getOrganization() {
        return organization;
    }

    public void setOrganization(@NotNull Organization organization) {
        this.organization = organization;
    }

    public @NotNull Float getSalary() {
        return salary;
    }

    public void setSalary(@NotNull Float salary) {
        this.salary = salary;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public @NotNull LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }


    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public @NotNull Person getPerson() {
        return person;
    }

    public void setPerson(@NotNull Person person) {
        this.person = person;
    }


    public void setStartDate(@NotNull LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
