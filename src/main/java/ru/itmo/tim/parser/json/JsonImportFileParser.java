package ru.itmo.tim.parser.json;

import ru.itmo.tim.enums.FileFormat;
import ru.itmo.tim.exception.WorkerValidationException;
import ru.itmo.tim.parser.WorkerImportFileParser;
import ru.itmo.tim.parser.upload.UploadWorker;
import ru.itmo.tim.utils.ParserForFloatValue;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbException;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class JsonImportFileParser implements WorkerImportFileParser {
    @Inject
    private Validator validator;
    @Inject
    private ParserForFloatValue parser;
    @Override
    public List<UploadWorker> parse(InputStream inputStream){
        try {
            Jsonb jsonb = JsonbBuilder.create();
            if (inputStream.available() == 0) {
                throw new IllegalArgumentException("Файл пуст");
            }
            UploadWorker[] arr = jsonb.fromJson(inputStream, UploadWorker[].class);
            for (UploadWorker worker : arr){
                validateWorker(worker);
                System.out.println(worker);
            }
            return Arrays.asList(arr);
        }catch(JsonbException e){
            String errorMessage = "Некорректный формат JSON в файле: ";
            if (e.getMessage().contains("java.lang.Integer")) {
                errorMessage += "Ошибка в поле: значение слишком большое для типа Integer.";
            }
            throw new IllegalArgumentException(errorMessage);
        }catch(IOException e){
            throw new IllegalArgumentException("Ошибка чтения файла", e);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }
    public void validateWorker(UploadWorker worker){
        Set<ConstraintViolation<UploadWorker>> violations = validator.validate(worker);
        Set<String> customViolations = new HashSet<>();
        if (worker.getOrganization()!=null){
            if (worker.getOrganization().getAnnualTurnover()!=null){
                if (!Float.isFinite(worker.getOrganization().getAnnualTurnover())) customViolations.add(" Organization - поле annualTurnover(годовой оборот): слишком большое числовое значение; ");
            }
            if (worker.getOrganization().getRating()!=null){
                if (!Float.isFinite(worker.getOrganization().getRating())) customViolations.add(" Organization - поле Rating (рейтинг): слишком большое числовое значение; ");
            }
        }
        if (worker.getSalary()!=null) {
            if (!Float.isFinite(worker.getSalary())) customViolations.add("salary: слишком большое числовое значение; ");
        }
        if (!violations.isEmpty() || !customViolations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("Ошибки валидации для работника: " + worker.getName() + "\n");
            for (ConstraintViolation<UploadWorker> violation : violations) {
                errorMessage.append(violation.getPropertyPath()).append(": ").append(violation.getMessage()).append("; ");
            }
            for (String s : customViolations){
                errorMessage.append(s);
            }
            throw new WorkerValidationException(errorMessage.toString());
        }
    }
    @Override
    public boolean supports(FileFormat fileFormat) {
        return FileFormat.JSON.equals(fileFormat);
    }
}
