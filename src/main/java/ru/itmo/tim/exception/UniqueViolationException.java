package ru.itmo.tim.exception;

public class UniqueViolationException extends DomainException{
    public UniqueViolationException(String msg){
        super(msg);
    }
}
