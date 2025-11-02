package app.responseDto;

import app.Position;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class WorkerResponseDto {
    private Integer id;
    private String name;
    private CoordinatesResponseDto coordinates;
    private LocalDate creationDate;
    private Long organizationId;
    //private String organizationName;
    private Float salary;
    private Integer rating;
    private LocalDateTime startDate;
    private LocalDate endDate;
    private Position position;
    private Long personId;
    public WorkerResponseDto() {}

    public Integer getId() {
        return id;
    }

    public WorkerResponseDto(Integer id, String name, CoordinatesResponseDto coordinates, LocalDate creationDate, Long organizationId, String organizationName, Float salary, Integer rating, LocalDateTime startDate, LocalDate endDate, Position position, Long personId) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.organizationId = organizationId;
        //this.organizationName = organizationName;
        this.salary = salary;
        this.rating = rating;
        this.startDate = startDate;
        this.endDate = endDate;
        this.position = position;
        this.personId = personId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CoordinatesResponseDto getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(CoordinatesResponseDto coordinates) {
        this.coordinates = coordinates;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }


    public Float getSalary() {
        return salary;
    }

    public void setSalary(Float salary) {
        this.salary = salary;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
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

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }
    /*
    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
    */
}
