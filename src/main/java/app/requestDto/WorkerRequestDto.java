package app.requestDto;


import app.Position;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class WorkerRequestDto {
    @NotBlank
    private String name;
    @NotNull
    @Valid
    private CoordinatesRequestDto coordinates;
    @NotNull
    private Long organizationId;
    @NotNull
    @Positive
    private Float salary;
    @Positive
    private int rating;
    @NotNull
    private Position position;
    @NotNull
    private Long personId;
    @NotNull
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
