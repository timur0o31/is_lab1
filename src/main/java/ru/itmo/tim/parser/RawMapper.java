package ru.itmo.tim.parser;

import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import ru.itmo.tim.parser.raw.RawCoordinates;
import ru.itmo.tim.parser.raw.RawLocation;
import ru.itmo.tim.parser.raw.RawPerson;
import ru.itmo.tim.parser.raw.RawWorker;
import ru.itmo.tim.requestDto.CoordinatesRequestDto;
import ru.itmo.tim.requestDto.LocationRequestDto;
import ru.itmo.tim.requestDto.PersonRequestDto;
import ru.itmo.tim.requestDto.WorkerRequestDto;

import java.util.List;

@Mapper(config = MapperConfig.class)
public interface RawMapper {

    @Mapping(target = "directorReference", source = "director")
    @Mapping(target = "screenwriterReference", source = "screenwriter")
    @Mapping(target = "operatorReference", source = "operator")
    WorkerRequestDto toWorkerRequestDto(RawWorker rawWorker);

    List<WorkerRequestDto> toWorkerRequestDto(List<RawWorker> rawWorkers);

    PersonRequestDto toPersonRequestDto(RawPerson rawPerson);

    LocationRequestDto toLocationRequestDto(RawLocation rawLocation);

    CoordinatesRequestDto toCoordinatesRequestDto(RawCoordinates rawCoordinates);

    default EmbeddedObjectDto<Long, PersonRequestDto> map(RawPerson person) {
        if (person == null) {
            return new EmbeddedObjectDto<>(null, null);
        }
        EmbeddedObjectDto<Long, PersonRequestDto> result = new EmbeddedObjectDto<>();
        result.setId(null);
        result.setValue(toPersonRequestDto(person));
        return result;
    }
}