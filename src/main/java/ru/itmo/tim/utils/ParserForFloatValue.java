package ru.itmo.tim.utils;

import ru.itmo.tim.exception.FieldValidationException;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ParserForFloatValue {
    public static Float safeFloat(Float value, String field){
        if (value == null){
            return null;
        }
        if (!Float.isFinite(value)){
            throw new FieldValidationException("Для поля: "+ field + " значение слишком большое, оно недопустимо");
        }
        return value;
    }

}
