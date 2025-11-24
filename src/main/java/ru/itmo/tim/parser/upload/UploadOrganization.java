package ru.itmo.tim.parser.upload;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UploadOrganization {
    @Valid
    @NotNull
    private UploadAddress officialAddress;
    @Positive(message="годовой оборот должен быть больше 0")
    private Float annualTurnover;
    @Positive(message="количество работников должно быть больше 0")
    private Long employeesCount;
    @NotNull(message="поле не может быть null")
    @Size(max = 550, message="длина строки не может больше 550")
    private String fullName;
    @NotNull(message="рейтинг не может быть null")
    @Positive(message="рейтинг должен быть положительным")
    private Float rating;
    @Valid
    @NotNull
    private UploadAddress postalAddress;
}
