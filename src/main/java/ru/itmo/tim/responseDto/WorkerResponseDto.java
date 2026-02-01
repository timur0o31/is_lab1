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
    private String creationDate;
    private Long organizationId;
    //private String organizationName;
    private Float salary;
    private Integer rating;
    private String startDate;
    private String endDate;
    private Position position;
    private Long personId;
    public WorkerResponseDto() {}
}
