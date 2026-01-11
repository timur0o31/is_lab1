package ru.itmo.tim.exception;

public class WorkerValidationException extends RuntimeException {
    private final String errorMessage;
    public WorkerValidationException(String message) {
        super(message);
        this.errorMessage = message;
    }
    public String getErrorMessage(){
        return this.errorMessage;
    }
}
