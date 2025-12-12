package ru.itmo.tim.service;

import ru.itmo.tim.dao.PersonDao;
import ru.itmo.tim.entity.Person;
import ru.itmo.tim.exception.DomainException;
import ru.itmo.tim.mapper.PersonMapper;
import ru.itmo.tim.requestDto.PersonRequestDto;
import ru.itmo.tim.responseDto.PersonResponseDto;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Map;


@Stateless
public class PersonService {
    @Inject
    private PersonDao personDao;
    @Inject
    private PersonMapper personMapper;
    public PersonService() {
    }

    public PersonResponseDto addPerson(PersonRequestDto personRequestDto) {
            Person person = personMapper.toCreateEntity(personRequestDto);
            personDao.save(person);
            return personMapper.toResponseDto(person);
    }

    public PersonResponseDto updatePerson(Long id, PersonRequestDto personRequestDto) {
        Person person = personDao.find(id);
        if (person == null) {
            throw new IllegalArgumentException("Person not found");
        }
        personMapper.toUpdateEntity(person, personRequestDto);
        personDao.update(person);
        return personMapper.toResponseDto(person);
    }

    public void deletePerson(Long id) {
        Person person = personDao.find(id);
        if (person == null) {
            throw new IllegalArgumentException("Человек не найден");
        }
        if (personDao.hasWorkers(id)){
            throw new DomainException("Нельзя удалить Person, пока на него ссылаются workers");
        }
        personDao.delete(person);
    }
    public PersonResponseDto getPerson(Long id) {
        return personMapper.toResponseDto(this.getPersonEntity(id));
    }
    public Person getPersonEntity(Long id){
        Person person = personDao.find(id);
        if (person == null) {
            throw new IllegalArgumentException("Человек не найден");
        }
        return person;
    }

    public List<PersonResponseDto> getAllPersons(int page, int size, String sortColumn, boolean sortDirection, Map<String, Object> filters){
        return personDao.getAll(page, size, sortColumn, sortDirection, filters).stream()
                .map(personMapper::toResponseDto)
                .toList();
    }
    public Long getCount(Map<String, Object> filters){
        return personDao.countAll(filters);
    }

}