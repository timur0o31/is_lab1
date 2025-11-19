package ru.itmo.tim.exception;
import javax.ejb.ApplicationException;

@ApplicationException(rollback=true)
public class FieldValidationException extends RuntimeException{
    public FieldValidationException(String message) {
        super(message);
    }
}
