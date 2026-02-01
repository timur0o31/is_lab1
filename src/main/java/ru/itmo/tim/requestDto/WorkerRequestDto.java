package ru.itmo.tim.requestDto;


import lombok.Getter;
import lombok.Setter;
import ru.itmo.tim.enums.Position;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Getter
@Setter
public class WorkerRequestDto {
    @NotBlank(message="Поле для имени должно быть заполнено")
    private String name;
    @NotNull(message = "Координаты не могут быть null")
    @Valid
    private CoordinatesRequestDto coordinates;
    @NotNull(message="Организация должна быть указана, не может быть null")
    private Long organizationId;
    @NotNull(message="Зарплата не может быть null")
    @Positive(message="Зарплата должна быть больше 0")
    private Float salary;
    @Positive(message="Рейтинг должен быть положителен")
    private int rating;

    private Position position;
    @NotNull(message="Персона должна быть указана")
    private Long personId;
    @NotNull(message="Дата начала работы не может быть null")
    private String startDate;
    private String endDate;
    public WorkerRequestDto() {}
}
