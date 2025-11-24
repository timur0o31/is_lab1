package ru.itmo.tim.service;

import ru.itmo.tim.dao.PersonDao;
import ru.itmo.tim.entity.Person;
import ru.itmo.tim.mapper.PersonMapper;
import ru.itmo.tim.parser.UploadMapper;
import ru.itmo.tim.parser.upload.UploadPerson;
import ru.itmo.tim.requestDto.PersonRequestDto;
import ru.itmo.tim.responseDto.PersonResponseDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Transactional
@ApplicationScoped
public class PersonService {
    @Inject
    private PersonDao personDao;
    @Inject
    private PersonMapper personMapper;
    @Inject
    private UploadMapper uploadMapper;
    public PersonService() {
    }

    public PersonResponseDto createPerson(PersonRequestDto personRequestDto) {
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
            throw new IllegalArgumentException("Person not found");
        }
        personDao.delete(person);
    }
    public PersonResponseDto getPerson(Long id) {
        return personMapper.toResponseDto(this.getPersonEntity(id));
    }
    public Person getPersonEntity(Long id){
        Person person = personDao.find(id);
        if (person == null) {
            throw new IllegalArgumentException("Person not found");
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
    public Person createPersonFromImport(UploadPerson upload){
        Person person = uploadMapper.toEntity(upload);
        if (upload.getLocation() != null){
            person.setLocation(uploadMapper.toEntity(upload.getLocation()));
        }
        personDao.save(person);
        return person;
    }

}