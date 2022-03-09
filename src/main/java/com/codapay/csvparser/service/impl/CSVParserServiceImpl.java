package com.codapay.csvparser.service.impl;

import com.codapay.csvparser.controller.request.CSVParserRequest;
import com.codapay.csvparser.controller.response.CSVParserResponse;
import com.codapay.csvparser.exception.BadRequestException;
import com.codapay.csvparser.exception.FileNotFoundException;
import com.codapay.csvparser.service.CSVParserService;
import com.codapay.csvparser.util.CsvParserUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.codapay.csvparser.enums.FormatType.JSON;

@Singleton
@Slf4j
public class CSVParserServiceImpl implements CSVParserService {

    private static final String OUTPUT_DIR= "/csv_parser_output";
    private static final String FILE_NAME= "%s/%s_%s_json.txt";

    @Override
    public CSVParserResponse parseToJson(CSVParserRequest csvParserRequest) {
        log.info("Method convertToJson() {}", csvParserRequest);

        if (JSON != csvParserRequest.getFormat()) {
            String errorMsg = String.format("CSV to %s not supported", csvParserRequest.getFormat());
            log.error(errorMsg);
            throw new BadRequestException(errorMsg);
        }

        final String pathToCsv = csvParserRequest.getPath();
        final Path path = Paths.get(pathToCsv);

        String pathFilename = path.getFileName().toString();

        if (!Files.exists(path)) {
            String errorMsg = String.format("File not found on the provided path %s", pathToCsv);
            log.error(errorMsg);
            throw new FileNotFoundException(errorMsg);
        }
        final String pathToOutputDirectory = pathToCsv.substring(0, pathToCsv.lastIndexOf("/")) + OUTPUT_DIR;
        log.info("Path to input file {} and path to output directory {}", path, pathToOutputDirectory);

        final String filename = pathFilename.contains(".") ? pathFilename.substring(0, pathFilename.lastIndexOf(".")) : pathFilename;

        try {
            var recordMap = CsvParserUtil.readCsvAndConvertToDataMap(pathToCsv);
            if (recordMap.size() == 0) {
                log.error("The csv file is empty or has only headers");
                throw new BadRequestException("The csv file is empty or has only headers");
            }
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            CsvParserUtil.createDirectory(pathToOutputDirectory);
            List<String> filePaths = new ArrayList<>();
            for (Integer key : recordMap.keySet()) {
                String newPath = String.format(FILE_NAME, pathToOutputDirectory, key.toString(), filename);
                String json = ow.writeValueAsString(recordMap.get(key));
                CsvParserUtil.writeJsonToFile(json, newPath);
                filePaths.add(newPath);
            }

            return CSVParserResponse.builder()
                    .jsonFiles(filePaths).build();
        } catch (IOException e) {
            log.error("Exception while reading/writing csv data", e);
            throw new RuntimeException("Exception while reading/writing csv data", e);
        }
    }

}
