package com.codapay.csvparser.rest;

import com.codapay.csvparser.controller.request.CSVParserRequest;
import com.codapay.csvparser.controller.response.CSVParserResponse;
import com.codapay.csvparser.controller.response.ErrorResponse;
import com.codapay.csvparser.util.CsvParserUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@MicronautTest
@DisplayName("CSV Parser REST API Tests")
@TestInstance(PER_CLASS)
@Tag("IntegrationTest")
public class CSVParserControllerTest {

    private ObjectMapper objectMapper;

    @Inject
    @Client("/")
    HttpClient client;

    private static final String API_PATH = "/api/csv-parser";
    private static final String CSV_FILE_PATH = "/Users/shri/csv-parser/src/test/resources/data.csv";
    private static final String CSV_EMPTY_FILE_PATH = "/Users/shri/csv-parser/src/test/resources/data_empty.csv";
    private static final String CSV_ONLY_HEADERS_PATH = "/Users/shri/csv-parser/src/test/resources/data_only_headers.csv";

    @BeforeAll
    public void init() {
        objectMapper = new ObjectMapper();
    }

    @SneakyThrows
    @Test
    @DisplayName("given valid location input, when create a json file, then location of the json file is returned")
    void givenLocation_whenCreateJsonFile_ThenOutputFileLocationReturned() {
        //given
        var csvLines = CsvParserUtil.readCSVAndReturnLines(CSV_FILE_PATH);
        int expectedCSVRows = csvLines.size() - 1; //skip header so -1
        String jsonInString = "{\"path\":\""+ CSV_FILE_PATH +"\",\"format\":\"JSON\" }";
        CSVParserRequest csvParserRequest = objectMapper.readValue(jsonInString, CSVParserRequest.class);
        HttpRequest<CSVParserRequest> request = HttpRequest.POST(API_PATH, csvParserRequest);

        //when
        final HttpResponse<CSVParserResponse> response = client.toBlocking().exchange(request, Argument.of(CSVParserResponse.class), Argument.of(ErrorResponse.class));

        //then
        assertEquals(HttpStatus.CREATED.getCode(), response.code());
        assertTrue(response.getBody().isPresent());
        assertEquals(expectedCSVRows, response.getBody().get().getJsonFiles().size());

    }

    @SneakyThrows
    @Test
    @DisplayName("given XML format type other than json, when create a json file, then exception is thrown")
    void givenXMLFormatType_whenCreateJsonFile_ThenThrowException() {
        //given
        String jsonInString = "{\"path\":\""+ CSV_FILE_PATH +"\",\"format\":\"xml\" }";
        CSVParserRequest csvParserRequest = objectMapper.readValue(jsonInString, CSVParserRequest.class);

        HttpRequest<CSVParserRequest> request = HttpRequest.POST(API_PATH, csvParserRequest);

        //when
        HttpClientResponseException exception = Assertions.assertThrows(HttpClientResponseException.class, () ->
                client.toBlocking().exchange(request, Argument.of(CSVParserResponse.class), Argument.of(ErrorResponse.class)));

        //then
        assertEquals(HttpStatus.BAD_REQUEST, exception.getResponse().getStatus());
    }

    @SneakyThrows
    @Test
    @DisplayName("given invalid format type, when create a json file, then success is returned")
    void givenInvalidFormatType_whenCreateJsonFile_ThenThrowException() {
        //given
        var csvLines = CsvParserUtil.readCSVAndReturnLines(CSV_FILE_PATH);
        int expectedCSVRows = csvLines.size() - 1; //skip header so -1
        String jsonInString = "{\"path\":\""+ CSV_FILE_PATH +"\",\"format\":\"aaa\" }";
        CSVParserRequest csvParserRequest = objectMapper.readValue(jsonInString, CSVParserRequest.class);
        HttpRequest<CSVParserRequest> request = HttpRequest.POST(API_PATH, csvParserRequest);

        //when
        final HttpResponse<CSVParserResponse> response = client.toBlocking().exchange(request, Argument.of(CSVParserResponse.class), Argument.of(ErrorResponse.class));

        //then
        assertEquals(HttpStatus.CREATED.getCode(), response.code());
        assertTrue(response.getBody().isPresent());
        assertEquals(expectedCSVRows, response.getBody().get().getJsonFiles().size());
    }

    @SneakyThrows
    @Test
    @DisplayName("given invalid file path, when create a json file, then exception is thrown")
    void givenInvalidFilePath_whenCreateJsonFile_ThenThrowException() {
        //given
        String jsonInString = "{\"path\":\"csv\",\"format\":\"JSON\" }";
        CSVParserRequest csvParserRequest = objectMapper.readValue(jsonInString, CSVParserRequest.class);

        HttpRequest<CSVParserRequest> request = HttpRequest.POST(API_PATH, csvParserRequest);

        //when
        HttpClientResponseException exception = Assertions.assertThrows(HttpClientResponseException.class, () ->
                client.toBlocking().exchange(request, Argument.of(CSVParserResponse.class), Argument.of(ErrorResponse.class)));

        //then
        assertEquals(HttpStatus.NOT_FOUND, exception.getResponse().getStatus());
    }

    @SneakyThrows
    @Test
    @DisplayName("given empty path, when create a json file, then exception is thrown")
    void givenEmptyPath_whenCreateJsonFile_ThenThrowException() {
        //given
        String jsonInString = "{\"path\":\"\",\"format\":\"JSON\" }";
        CSVParserRequest csvParserRequest = objectMapper.readValue(jsonInString, CSVParserRequest.class);

        HttpRequest<CSVParserRequest> request = HttpRequest.POST(API_PATH, csvParserRequest);

        //when
        HttpClientResponseException exception = Assertions.assertThrows(HttpClientResponseException.class, () ->
                client.toBlocking().exchange(request, Argument.of(CSVParserResponse.class), Argument.of(ErrorResponse.class)));

        //then
        assertEquals(HttpStatus.BAD_REQUEST, exception.getResponse().getStatus());
    }

    @SneakyThrows
    @Test
    @DisplayName("given empty csv file, when create a json file, then exception is thrown")
    void givenEmptyFile_whenCreateJsonFile_ThenThrowException() {
        //given
        String jsonInString = "{\"path\":\""+ CSV_EMPTY_FILE_PATH +"\",\"format\":\"JSON\" }";
        CSVParserRequest csvParserRequest = objectMapper.readValue(jsonInString, CSVParserRequest.class);

        HttpRequest<CSVParserRequest> request = HttpRequest.POST(API_PATH, csvParserRequest);

        //when
        HttpClientResponseException exception = Assertions.assertThrows(HttpClientResponseException.class, () ->
                client.toBlocking().exchange(request, Argument.of(CSVParserResponse.class), Argument.of(ErrorResponse.class)));

        //then
        assertEquals(HttpStatus.BAD_REQUEST, exception.getResponse().getStatus());
    }

    @SneakyThrows
    @Test
    @DisplayName("given csv file with only headers, when create a json file, then exception is thrown")
    void givenFileWithHeaders_whenCreateJsonFile_ThenThrowException() {
        //given
        String jsonInString = "{\"path\":\""+ CSV_ONLY_HEADERS_PATH +"\",\"format\":\"JSON\" }";
        CSVParserRequest csvParserRequest = objectMapper.readValue(jsonInString, CSVParserRequest.class);

        HttpRequest<CSVParserRequest> request = HttpRequest.POST(API_PATH, csvParserRequest);

        //when
        HttpClientResponseException exception = Assertions.assertThrows(HttpClientResponseException.class, () ->
                client.toBlocking().exchange(request, Argument.of(CSVParserResponse.class), Argument.of(ErrorResponse.class)));

        //then
        assertEquals(HttpStatus.BAD_REQUEST, exception.getResponse().getStatus());
    }

}
