package ru.itmo.tim.responseDto;

import lombok.Getter;
import lombok.Setter;
import ru.itmo.tim.enums.Position;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
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
}
