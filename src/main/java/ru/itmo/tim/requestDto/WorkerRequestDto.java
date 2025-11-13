package ru.itmo.tim.requestDto;


import ru.itmo.tim.enums.Position;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class WorkerRequestDto {
    @NotBlank(message="Поле для имени должно быть заполнено")
    private String name;
    @NotNull
    @Valid
    private CoordinatesRequestDto coordinates;
    @NotNull(message="Организация должна быть указана, не может быть null")
    private Long organizationId;
    @NotNull
    @Positive(message="Зарплата должна быть больше 0")
    private Float salary;
    @Positive(message="Рейтинг должен быть положителен")
    private int rating;

    private Position position;
    @NotNull(message="Персона должна быть указана")
    private Long personId;
    @NotNull(message="Дата начала работы не может быть null")
    private LocalDateTime startDate;
    private LocalDate endDate;
    public WorkerRequestDto() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getSalary() {
        return salary;
    }

    public void setSalary(Float salary) {
        this.salary = salary;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public Long getPersonId() {
        return personId;
    }

    public CoordinatesRequestDto getCoordinates() {
        return coordinates;
    }


    public void setCoordinates(CoordinatesRequestDto coordinates) {
        this.coordinates = coordinates;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
