package ru.itmo.tim.mapper;

import ru.itmo.tim.entity.ImportOperation;
import ru.itmo.tim.responseDto.ImportOperationResponseDto;

public class ImportOperationMapper {
    public ImportOperationMapper() {}
    public ImportOperationResponseDto toResponseDto(ImportOperation importOperation) {
        if (importOperation == null) {
            return null;
        }
        ImportOperationResponseDto dto = new ImportOperationResponseDto();
        dto.setId(importOperation.getId());
        dto.setStatus(importOperation.getStatus());
        dto.setCount(importOperation.getCount());
        return dto;
    }


}
