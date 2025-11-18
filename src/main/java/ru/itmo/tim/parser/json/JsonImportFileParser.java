package ru.itmo.tim.parser.json;

import ru.itmo.tim.enums.FileFormat;
import ru.itmo.tim.parser.WorkerImportFileParser;
import ru.itmo.tim.parser.raw.RawWorker;
import ru.itmo.tim.requestDto.WorkerRequestDto;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class JsonImportFileParser implements WorkerImportFileParser {
    @Inject
    private RawJsonMapper rawJsonMapper;

    @Override
    public List<WorkerRequestDto> parse(InputStream inputStream){
        Jsonb jsonb = JsonbBuilder.create();
        RawWorker[] arr = jsonb.fromJson(inputStream, RawWorker[].class);
        return rawJsonMapper.toRequestDto(Arrays.asList(arr));
    }
    @Override
    public boolean supports(FileFormat fileFormat) {
        return FileFormat.JSON.equals(fileFormat);
    }
}
