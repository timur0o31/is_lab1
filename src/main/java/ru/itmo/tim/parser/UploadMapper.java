package ru.itmo.tim.parser;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.itmo.tim.entity.*;
import ru.itmo.tim.parser.upload.*;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface UploadMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "person", ignore = true)
    Worker toEntity(UploadWorker upload);

    Coordinates toEntity(UploadCoordinates coordinates);

    Organization toEntity(UploadOrganization organization);

    Address toEntity(UploadAddress address);

    Person toEntity(UploadPerson person);

    Location toEntity(UploadLocation location);

    List<Worker> toEntity(List<UploadWorker> workers);
}
