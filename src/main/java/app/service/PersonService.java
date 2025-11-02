package app.service;

import app.dao.PersonDao;
import app.entities.Person;
import app.mappers.PersonMapper;
import app.requestDto.PersonRequestDto;
import app.responseDto.PersonResponseDto;

import javax.ejb.Stateless;
import javax.inject.Inject;
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

}