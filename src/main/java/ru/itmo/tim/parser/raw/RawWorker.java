package ru.itmo.tim.parser.raw;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.itmo.tim.enums.Position;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RawWorker {
    private String name;
    private RawCoordinates coordinates;
    private RawOrganization organization;
    private Float salary;
    private int rating;
    private Position position;
    private RawPerson person;
    private LocalDateTime startDate;

}
