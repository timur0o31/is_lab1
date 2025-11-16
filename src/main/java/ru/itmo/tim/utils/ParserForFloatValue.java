package ru.itmo.tim.utils;

public class ParserForFloatValue {
    public static Float safeFloat(Float value, String field){
        if (value == null){
            return null;
        }
        if (!Float.isFinite(value)){
            throw new NumberFormatException("Для поля: "+ field + " значение слишком большое, оно недопустимо");
        }
        return value;
    }
}
