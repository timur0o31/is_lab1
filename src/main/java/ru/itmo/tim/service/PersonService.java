package ru.itmo.tim.service;

import ru.itmo.tim.DatabaseInitializier;
import ru.itmo.tim.cache.CacheStatisticsLogging;
import ru.itmo.tim.dao.PersonDao;
import ru.itmo.tim.entity.Person;
import ru.itmo.tim.exception.DomainException;
import ru.itmo.tim.exception.UniqueViolationException;
import ru.itmo.tim.mapper.PersonMapper;
import ru.itmo.tim.parser.UploadMapper;
import ru.itmo.tim.requestDto.PersonRequestDto;
import ru.itmo.tim.responseDto.PersonResponseDto;
import ru.itmo.tim.utils.TxIsolation;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Map;

@CacheStatisticsLogging
@ApplicationScoped
public class PersonService {
    @Inject
    private PersonDao personDao;
    @Inject
    private PersonMapper personMapper;
    public PersonResponseDto createPerson(PersonRequestDto personRequestDto) {
        EntityManager em = DatabaseInitializier.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            this.checkCreateUniqueConstraint(em,personRequestDto.getPassportId());
            Person person = personMapper.toCreateEntity(personRequestDto);
            personDao.save(em,person);
            tx.commit();
            return personMapper.toResponseDto(person);
        }catch(Exception e){
            if (tx.isActive()) tx.rollback();
            throw e;
        }finally{
            em.close();
        }
    }
    public PersonResponseDto updatePerson(Long id, PersonRequestDto personRequestDto) {
        EntityManager em = DatabaseInitializier.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Person person = personDao.find(em,id);
            if (person == null) {
                throw new IllegalArgumentException("Person not found");
            }
            this.checkUpdateUniqueConstraint(em, person, personRequestDto.getPassportId());
            personMapper.toUpdateEntity(person, personRequestDto);
            personDao.update(person);
            return personMapper.toResponseDto(person);
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
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

    public void checkCreateUniqueConstraint(EntityManager em, String passportId){
        if (personDao.existByPassportId(em, passportId)!=null){
            throw new UniqueViolationException("Человек с данным passportId уже существует!");
        }
    }
    public void checkUpdateUniqueConstraint(EntityManager em,Person person, String passportId){
        Person personDB = personDao.existByPassportId(em, passportId);
        if (personDB!= null && personDB.getId()!=person.getId()){
            throw new UniqueViolationException("Человек с данным passportId уже существует!");
        }
    }

    public PersonResponseDto getPersonByPassportId(String passportId){
        return personMapper.toResponseDto(personDao.existByPassportId(passportId));
    }
}