package ru.itmo.tim.mapper;

import ru.itmo.tim.entity.Person;
import ru.itmo.tim.requestDto.PersonRequestDto;
import ru.itmo.tim.responseDto.PersonResponseDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class PersonMapper {
    @Inject
    private LocationMapper locationMapper;
    public PersonMapper(){}
    public PersonResponseDto toResponseDto(Person person){
        PersonResponseDto dto = new PersonResponseDto();
        dto.setId(person.getId());
        dto.setEyeColor(person.getEyeColor());
        dto.setHairColor(person.getHairColor());
        dto.setLocation(locationMapper.toResponseDto(person.getLocation()));
        dto.setPassportId(person.getPassportId());
        dto.setNationality(person.getNationality());
        return dto;
    }
    public Person toCreateEntity(PersonRequestDto dto){
        if (dto == null){
            return null;
        }
        Person person = new Person();
        person.setEyeColor(dto.getEyeColor());
        person.setHairColor(dto.getHairColor());
        person.setPassportId(dto.getPassportId());
        person.setNationality(dto.getNationality());
        person.setLocation(locationMapper.toCreate(dto.getLocation()));
        return person;
    }

    public void toUpdateEntity(Person person, PersonRequestDto dto){
        if (person == null || dto == null){
            return ;
        }
        person.setEyeColor(dto.getEyeColor());
        person.setHairColor(dto.getHairColor());
        person.setPassportId(dto.getPassportId());
        person.setNationality(dto.getNationality());
        person.setLocation(locationMapper.toCreate(dto.getLocation()));
    }
}
