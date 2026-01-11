package ru.itmo.tim.responseDto;

import lombok.Getter;
import lombok.Setter;
import ru.itmo.tim.enums.Status;

@Getter
@Setter
public class ImportOperationResponseDto {
    private Long id;
    private Status status;
    private Long count;
    private String message;
    public ImportOperationResponseDto() {}
}
