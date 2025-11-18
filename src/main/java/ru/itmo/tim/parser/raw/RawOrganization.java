package ru.itmo.tim.parser.raw;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RawOrganization {
    private RawAddress officialAddress;
    private Float annualTurnover;
    private Long employeesCount;
    private String fullName;
    private Float rating;
}
