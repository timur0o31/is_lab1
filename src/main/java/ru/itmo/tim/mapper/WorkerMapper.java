package ru.itmo.tim.mapper;

import ru.itmo.tim.entity.Worker;
import ru.itmo.tim.requestDto.WorkerRequestDto;
import ru.itmo.tim.responseDto.WorkerResponseDto;
import ru.itmo.tim.utils.ParserForFloatValue;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class WorkerMapper {
    @Inject
    private CoordinatesMapper coordinatesMapper;
    @Inject
    private OrganizationMapper organizationMapper;
    @Inject
    private PersonMapper personMapper;
    public WorkerMapper(){
    }
    public WorkerResponseDto toResponseDto(Worker worker) {
        WorkerResponseDto responseDto = new WorkerResponseDto();
        responseDto.setId(worker.getId());
        responseDto.setName(worker.getName());
        responseDto.setCoordinates(coordinatesMapper.toResponseDto(worker.getCoordinates()));
        responseDto.setCreationDate(worker.getCreationDate());
        responseDto.setOrganizationId(worker.getOrganization().getId());
        responseDto.setPosition(worker.getPosition());
        responseDto.setSalary(worker.getSalary());
        responseDto.setRating(worker.getRating());
        responseDto.setStartDate(worker.getStartDate());
        responseDto.setEndDate(worker.getEndDate());
        responseDto.setPersonId(worker.getPerson().getId());
        return responseDto;
    }
    public Worker toCreateEntity(WorkerRequestDto dto) {
        if (dto == null) return null;
        Worker worker = new Worker();
        worker.setName(dto.getName());
        worker.setSalary(ParserForFloatValue.safeFloat(dto.getSalary(),"Зарплата"));
        worker.setRating(dto.getRating());
        worker.setPosition(dto.getPosition());
        worker.setStartDate(dto.getStartDate());
        worker.setEndDate(dto.getEndDate());
        worker.setCoordinates(coordinatesMapper.toCreate(dto.getCoordinates()));
        return worker;
    }
    public void toUpdateEntity(Worker worker, WorkerRequestDto dto) {
        if (worker == null ||dto == null) return;
        worker.setName(dto.getName());
        worker.setSalary(ParserForFloatValue.safeFloat(dto.getSalary(),"Зарплата"));
        worker.setRating(dto.getRating());
        worker.setPosition(worker.getPosition());
        worker.setStartDate(dto.getStartDate());
        worker.setEndDate(dto.getEndDate());
        worker.setCoordinates(coordinatesMapper.toCreate(dto.getCoordinates()));
    }
}
