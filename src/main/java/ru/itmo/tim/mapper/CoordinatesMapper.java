package ru.itmo.tim.mapper;

import ru.itmo.tim.entity.Coordinates;
import ru.itmo.tim.requestDto.CoordinatesRequestDto;
import ru.itmo.tim.responseDto.CoordinatesResponseDto;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CoordinatesMapper {
    public CoordinatesMapper() {}
    public CoordinatesResponseDto toResponseDto(Coordinates coordinates) {
        if (coordinates == null)
            return null;
        CoordinatesResponseDto dto = new CoordinatesResponseDto();
        dto.setX(coordinates.getX());
        dto.setY(coordinates.getY());
        return dto;
    }
    public Coordinates toCreate(CoordinatesRequestDto coordinates) {
        if (coordinates == null){
            return null;
        }
        Coordinates entity = new Coordinates();
        entity.setX(coordinates.getX());
        entity.setY(coordinates.getY());
        return entity;
    }
}
