package ru.itmo.tim.requestDto;

import ru.itmo.tim.enums.Position;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class HireWorkerRequestDto {
    @NotNull(message = "Необходимо выбрать человека")
    private Long personId;

    @NotNull(message="Организация должна быть указана, не может быть null")
    private Long organizationId;

    @NotBlank(message="Поле для имени должно быть заполнено")
    private String name;

    @NotNull(message = "Координаты не могут быть null")
    @Valid
    private CoordinatesRequestDto coordinates;

    @NotNull(message="Зарплата не может быть null")
    @Positive(message="Зарплата должна быть больше 0")
    private Float salary;

    @Positive
    private int rating;

    private Position position;

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CoordinatesRequestDto getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(CoordinatesRequestDto coordinates) {
        this.coordinates = coordinates;
    }

    public  Float getSalary() {
        return salary;
    }

    public void setSalary(Float salary) {
        this.salary = salary;
    }


    public int getRating() {
        return rating;
    }

    public void setRating( int rating) {
        this.rating = rating;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
