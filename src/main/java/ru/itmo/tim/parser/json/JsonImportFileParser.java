package ru.itmo.tim.parser.json;

import ru.itmo.tim.enums.FileFormat;
import ru.itmo.tim.parser.WorkerImportFileParser;
import ru.itmo.tim.parser.upload.UploadWorker;
import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class JsonImportFileParser implements WorkerImportFileParser {

    @Override
    public List<UploadWorker> parse(InputStream inputStream){
        Jsonb jsonb = JsonbBuilder.create();
        UploadWorker[] arr = jsonb.fromJson(inputStream, UploadWorker[].class);
        return Arrays.asList(arr);
    }
    @Override
    public boolean supports(FileFormat fileFormat) {
        return FileFormat.JSON.equals(fileFormat);
    }
}
