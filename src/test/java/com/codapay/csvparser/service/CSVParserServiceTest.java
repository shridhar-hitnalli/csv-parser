package com.codapay.csvparser.service;

import com.codapay.csvparser.controller.request.CSVParserRequest;
import com.codapay.csvparser.controller.response.CSVParserResponse;
import com.codapay.csvparser.enums.FormatType;
import com.codapay.csvparser.exception.BadRequestException;
import com.codapay.csvparser.exception.FileNotFoundException;
import com.codapay.csvparser.model.CSVData;
import com.codapay.csvparser.model.Field;
import com.codapay.csvparser.service.impl.CSVParserServiceImpl;
import com.codapay.csvparser.util.CsvParserUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
@Tag("UnitTest")
@DisplayName("CSVParser Service Unit Tests")
public class CSVParserServiceTest {

    private CSVParserService csvParserService;

    private ObjectMapper objectMapper;

    private static final String CSV_FILE_PATH = "/Users/shri/csv-parser/src/test/resources/data.csv";
    private static final String CSV_EMPTY_FILE_PATH = "/Users/shri/csv-parser/src/test/resources/data_empty.csv";
    private static final String CSV_ONLY_HEADERS_PATH = "/Users/shri/csv-parser/src/test/resources/data_only_headers.csv";
    private static final String CSV_INVALID_COMMAS_PATH = "/Users/shri/csv-parser/src/test/resources/data_invalid_commas.csv";

    @BeforeAll
    public void init() {
        csvParserService = new CSVParserServiceImpl() {};
        objectMapper = new ObjectMapper();
    }

    @SneakyThrows
    @Test
    @DisplayName("given valid csv location, when create a json file, then location of the json file is returned")
    void givenValidInput_whenCreateJsonFile_ThenOutputFileLocationReturned() {
        //given
        var csvLines = CsvParserUtil.readCSVAndReturnLines(CSV_FILE_PATH);
        int expectedCSVRows = csvLines.size() - 1; //skip header so -1
        CSVParserRequest csvParserRequest = CSVParserRequest.builder().path(CSV_FILE_PATH).build();

        //when
        final CSVParserResponse csvParserResponse = csvParserService.parseToJson(csvParserRequest);

        //then
        assertFalse(csvParserResponse.getJsonFiles().isEmpty());
        assertEquals(expectedCSVRows, csvParserResponse.getJsonFiles().size());

        CSVData obj = objectMapper.readValue(new File(csvParserResponse.getJsonFiles().get(0)), CSVData.class);

        List<String> jsonFieldNames = obj.getRecord().stream().map(Field::getFieldName).collect(Collectors.toList());
        List<String> csvHeaders = Arrays.stream(csvLines.get(0).split("\\s*,\\s*")).collect(Collectors.toList());
        assertTrue(jsonFieldNames.containsAll(csvHeaders));
    }

    @Test
    @DisplayName("given invalid format type other than json, when create a json file, then exception is thrown")
    void givenInvalidFormatType_whenCreateJsonFile_ThenThrowException() {
        //given
        CSVParserRequest csvParserRequest = CSVParserRequest.builder().path(CSV_FILE_PATH).format(FormatType.XML).build();
        String errorMsg = String.format("CSV to %s not supported", csvParserRequest.getFormat());

        //when
        RuntimeException throwException = assertThrows(BadRequestException.class, () ->  csvParserService.parseToJson(csvParserRequest));

        //then
        assertEquals(errorMsg, throwException.getMessage());
    }

    @Test
    @DisplayName("given invalid file path, when create a json file, then exception is thrown")
    void givenInvalidFilePath_whenCreateJsonFile_ThenThrowException() {
        //given
        String invalidPath = "/csv";
        CSVParserRequest csvParserRequest = CSVParserRequest.builder().path(invalidPath).build();
        String errorMsg =  String.format("File not found on the provided path %s", invalidPath);

        //when
        RuntimeException throwException = assertThrows(FileNotFoundException.class, () ->  csvParserService.parseToJson(csvParserRequest));

        //then
        assertEquals(errorMsg, throwException.getMessage());
    }

    @Test
    @DisplayName("given empty csv file, when create a json file, then exception is thrown")
    void givenEmptyFile_whenCreateJsonFile_ThenThrowException() {
        //given
        CSVParserRequest csvParserRequest = CSVParserRequest.builder().path(CSV_EMPTY_FILE_PATH).build();
        String errorMsg =  "The csv file is empty";

        //when
        RuntimeException throwException = assertThrows(BadRequestException.class, () ->  csvParserService.parseToJson(csvParserRequest));

        //then
        assertEquals(errorMsg, throwException.getMessage());
    }

    @Test
    @DisplayName("given csv file with only headers, when create a json file, then exception is thrown")
    void givenFileWithHeaders_whenCreateJsonFile_ThenThrowException() {
        //given
        CSVParserRequest csvParserRequest = CSVParserRequest.builder().path(CSV_ONLY_HEADERS_PATH).build();
        String errorMsg =  "The csv file is empty or has only headers";

        //when
        RuntimeException throwException = assertThrows(BadRequestException.class, () ->  csvParserService.parseToJson(csvParserRequest));

        //then
        assertEquals(errorMsg, throwException.getMessage());
    }

    @SneakyThrows
    @Test
    @DisplayName("given csv file with spaces around the data, when create a json file, then location of the json file is returned")
    void givenFileWithHeaders_whenCreateJsonFile_ThenOutputFileLocationReturned() {
        //given
        CSVParserRequest csvParserRequest = CSVParserRequest.builder().path(CSV_INVALID_COMMAS_PATH).build();

        String errorMsg =  "The csv file is empty or has only headers";

        //when
        RuntimeException throwException = assertThrows(BadRequestException.class, () ->  csvParserService.parseToJson(csvParserRequest));

        //then
        assertEquals(errorMsg, throwException.getMessage());
    }
}
