package ru.itmo.tim.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback=true)
public class DomainException extends RuntimeException{
    public DomainException(String msg){
        super(msg);
    }
}
