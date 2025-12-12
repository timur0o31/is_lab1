package ru.itmo.tim.service;

import ru.itmo.tim.dao.WorkerDao;
import ru.itmo.tim.entity.Worker;
import ru.itmo.tim.exception.DomainException;
import ru.itmo.tim.mapper.WorkerMapper;
import ru.itmo.tim.requestDto.HireWorkerRequestDto;
import ru.itmo.tim.parser.UploadMapper;
import ru.itmo.tim.parser.upload.UploadWorker;
import ru.itmo.tim.requestDto.WorkerRequestDto;
import ru.itmo.tim.responseDto.WorkerResponseDto;
import ru.itmo.tim.utils.ParserForFloatValue;

import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
@Transactional
@ApplicationScoped
public class WorkerService {
    @Inject
    private WorkerDao workerDao;
    @Inject
    private WorkerMapper workerMapper;
    @Inject
    private PersonService personService;
    @Inject
    private OrganizationService organizationService;
    public WorkerResponseDto createWorker(WorkerRequestDto dto) {
        Worker worker = workerMapper.toCreateEntity(dto);
        //Worker other = workerDao.findByPersonId(dto.getPersonId());
        this.resolveDependencies(dto, worker);
        workerDao.save(worker);
        return workerMapper.toResponseDto(worker);
    }

    public WorkerResponseDto updateWorker(int id, WorkerRequestDto dto) {
            Worker worker = workerDao.find(id);
            if (worker == null) {
                throw new DomainException("Рабочего с таким id не существует");
            }
        //Worker other = workerDao.findByPersonId(dto.getPersonId());
            if (dto.getPersonId() != worker.getPerson().getId()) throw new DomainException("Изменение Person у существующего Worker запрещено.");
            workerMapper.toUpdateEntity(worker, dto);
            this.resolveDependencies(dto, worker);
            workerDao.update(worker);
            return workerMapper.toResponseDto(worker);
    }

    public void deleteWorker(int id) {
        Worker worker = workerDao.find(id);
        if (worker == null) {
            throw new IllegalArgumentException("Worker not found");
        }
        workerDao.delete(worker);
    }

    public WorkerResponseDto getWorker(Integer id) {
        Worker worker = workerDao.find(id);
        if (worker == null) {
            throw new IllegalArgumentException("Worker not found");
        }
        return workerMapper.toResponseDto(worker);
    }

    public List<Worker> getAll(int page, int size) {
        return workerDao.getAll(page, size);
    }

    public List<WorkerResponseDto> getAll(int page, int size,Map<String, Object> filters, String sortColumn, boolean asc) {
        return workerDao.getAll(page, size, filters, sortColumn, asc).stream()
                .map(workerMapper::toResponseDto)
                .toList();
    }
    public Long getCount(Map<String, Object> filters){
        return workerDao.countAll(filters);
    }
    private void resolveDependencies(WorkerRequestDto dto, Worker worker) {
        if (dto.getPersonId() != null) {
            worker.setPerson(personService.getPersonEntity(dto.getPersonId()));
        } else {
            throw new IllegalArgumentException("Person not found");
        }
        if (dto.getOrganizationId() != null) {
            worker.setOrganization(organizationService.getOrganizationEntity(dto.getOrganizationId()));
        } else {
            throw new IllegalArgumentException("Organization not found");
        }
        if (worker.getStartDate()!=null && worker.getEndDate()!=null) {
            if (worker.getEndDate().isBefore(worker.getStartDate().toLocalDate())) throw new DomainException("Дата окончания работы не может быть раньше трудоустройства");
        }
    }

    public Double sumRating() {
        return workerDao.sumRatings();
    }

    public List<WorkerResponseDto> findByNamePrefix(String prefix) {
        return workerDao.findByNamePrefix(prefix).stream()
                .map(workerMapper::toResponseDto)
                .toList();
    }

    public List<WorkerResponseDto> findByEndDateAfter(LocalDate date) {
        return workerDao.findByEndDateAfter(date).stream()
                .map(workerMapper::toResponseDto)
                .toList();
    }

    public List<WorkerResponseDto> getUnemployedWorkers() {
        return workerDao.getUnemployedWorkers().stream()
                .map(workerMapper::toResponseDto)
                .toList();
    }

    public void hireWorker(HireWorkerRequestDto dto) {
        String name = null;
        if (dto.getPosition() != null) {
            name = dto.getPosition().name();
        }
        if(!workerDao.hireWorker(
                dto.getPersonId(),
                dto.getOrganizationId(),
                dto.getName(),
                ParserForFloatValue.safeFloat(dto.getSalary(),"salary"),
                dto.getRating(),
                name,
                dto.getCoordinates().getX(),
                dto.getCoordinates().getY()
        )) throw new DomainException("У работника уже есть активная работа");
    }

    public void fireWorker(int workerId) {
        Worker worker = workerDao.find(workerId);
        if (worker == null) {
            throw new IllegalArgumentException("Worker not found");
        }
        workerDao.fireWorker(workerId);
    }

    public List<WorkerResponseDto> findByOrganization(Long id) {
        return workerDao.findByOrganization(id).stream()
                .map(workerMapper::toResponseDto)
                .toList();
    }
}
