package ru.itmo.tim.requestDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.itmo.tim.enums.Position;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
@Getter
@Setter
@NoArgsConstructor
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
}
