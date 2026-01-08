package ru.itmo.tim.responseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressResponseDto {
    private Integer id;
    private String street;
    private String zipCode;
    public AddressResponseDto() {}
}
