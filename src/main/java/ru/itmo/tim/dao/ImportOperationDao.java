package ru.itmo.tim.dao;

import ru.itmo.tim.entity.ImportOperation;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ImportOperationDao extends GenericDao<ImportOperation>{
    public ImportOperationDao() {
        super(ImportOperation.class);
    }

}
