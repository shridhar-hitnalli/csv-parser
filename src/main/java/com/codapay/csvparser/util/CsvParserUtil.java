package com.codapay.csvparser.util;

import com.codapay.csvparser.exception.BadRequestException;
import com.codapay.csvparser.model.CSVData;
import com.codapay.csvparser.model.Field;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.codapay.csvparser.enums.Datatype.getTypeOfField;

@UtilityClass
@Slf4j
public class CsvParserUtil {

    private final String REGEX = "\\s*,\\s*"; //regex to split with comma and spaces around it

    public List<String> readCSVAndReturnLines(final String path) throws IOException {
        try (var reader = Files.lines(Paths.get(path))) {
            return reader.collect(Collectors.toList());
        }
    }

    public Map<Integer, CSVData> readCsvAndConvertToDataMap(final String path) throws IOException {
        List<String> csvRows;
        try (var reader = Files.lines(Paths.get(path))) {
            csvRows =  reader.collect(Collectors.toList());
        }

        if (csvRows.size() == 0) {
            log.error("The csv file is empty");
            throw new BadRequestException("The csv file is empty");
        }
        csvRows.removeIf(e -> e.trim().isEmpty()); //remove blank lines
        //csv is empty or have declared only columns
        if (csvRows.size() <= 1) {
            log.error("The csv file is empty or has only headers");
            throw new BadRequestException("The csv file is empty or has only headers");
        }
        //get columns names
        var columns = csvRows.get(0).split(REGEX);
        var map = new HashMap<Integer, CSVData>();
        AtomicInteger count = new AtomicInteger(1); // need count to generate the number for text files
        csvRows.subList(1, csvRows.size()) //ignoring the first row
                .stream()
                .map(e -> e.split(REGEX))
                .filter(e -> e.length == columns.length) //values size should match with field names size
                .forEach(row -> {
                    CSVData data = new CSVData();
                    for(int i = 0; i < columns.length; i++) {
                        data.getRecord().add(new Field(columns[i].strip(), row[i].strip(), getTypeOfField(row[i].strip())));
                    }
                    map.put(count.getAndIncrement(), data);
                });

        return map;

    }

    public void createDirectory(final String path) throws IOException {
        if (!Files.isDirectory(Paths.get(path))) {
            Files.createDirectory(Paths.get(path));
        }

    }

    public void writeJsonToFile(String json, String path) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(path), StandardCharsets.UTF_8)) {
            if (json != null && !json.isBlank()) {
                writer.write(json);
            }
        }
    }

}
