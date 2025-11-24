package ru.itmo.tim.parser.upload;

import lombok.Getter;
import lombok.Setter;
import ru.itmo.tim.enums.Position;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Getter
@Setter
public class UploadWorker {
    @NotBlank(message="Поле для имени должно быть заполнено")
    private String name;

    @NotNull
    @Valid
    private UploadCoordinates coordinates;

    @NotNull(message="Организация должна быть указана, не может быть null")
    private UploadOrganization organization;

    @NotNull
    @Positive(message="Зарплата должна быть больше 0")
    private Float salary;
    @Positive(message="Рейтинг должен быть положителен")
    private int rating;
    @Enumerated(EnumType.STRING)
    private Position position;
    @NotNull(message="Персона должна быть указана")
    private UploadPerson person;
    @NotNull(message="Дата начала работы не может быть null")
    private LocalDateTime startDate;
    private LocalDate endDate;
}
