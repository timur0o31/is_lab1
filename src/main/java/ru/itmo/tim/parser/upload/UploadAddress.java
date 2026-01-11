package ru.itmo.tim.parser.upload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UploadAddress {
    private String street;
    @Size(min = 4, message="длина почтового индекса должна быть больше 3")
    @NotNull(message="Поле не может быть null")
    private String zipCode;
    @Override
    public String toString() {
        return "UploadAddress{" +
                "street='" + street + '\'' +
                ", zipCode='" + zipCode + '\'' +
                '}';
    }

}
