package app.mappers;

import app.entities.Location;
import app.requestDto.LocationRequestDto;
import app.responseDto.LocationResponseDto;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LocationMapper {
    public LocationMapper(){
    }
    public LocationResponseDto toResponseDto(Location location){
        if (location == null) return null;
        LocationResponseDto dto = new LocationResponseDto();
        dto.setX(location.getX());
        dto.setY(location.getY());
        dto.setZ(location.getZ());
        return dto;
    }
    public Location toCreate(LocationRequestDto locationRequestDto){
        if (locationRequestDto == null) return null;
        Location location = new Location();
        location.setX(locationRequestDto.getX());
        location.setY(locationRequestDto.getY());
        location.setZ(locationRequestDto.getZ());
        return location;
    }
}
