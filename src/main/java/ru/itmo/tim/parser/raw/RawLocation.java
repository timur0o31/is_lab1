package ru.itmo.tim.parser.raw;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RawLocation {
    private Integer x;
    private Integer y;
    private Long z;
}
