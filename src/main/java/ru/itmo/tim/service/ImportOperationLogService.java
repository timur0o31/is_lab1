package ru.itmo.tim.service;

import ru.itmo.tim.dao.ImportOperationDao;
import ru.itmo.tim.entity.ImportOperation;
import ru.itmo.tim.enums.Status;
import ru.itmo.tim.mapper.ImportOperationMapper;
import ru.itmo.tim.responseDto.ImportOperationResponseDto;

import javax.inject.Inject;
import javax.transaction.Transactional;

public class ImportOperationLogService {
    @Inject
    private ImportOperationDao dao;
    @Inject
    private ImportOperationMapper mapper;
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public ImportOperationResponseDto persist(Status status, Long count, String message, String fileKey, String fileName) {
        ImportOperation op = new ImportOperation();
        op.setStatus(status);
        op.setCount(count);
        op.setMessage(message);
        op.setFileKey(fileKey);
        op.setFileName(fileName);
        dao.save(op);
        return mapper.toResponseDto(op);
    }
}
