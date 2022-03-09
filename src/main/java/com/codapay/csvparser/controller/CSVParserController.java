package com.codapay.csvparser.controller;

import com.codapay.csvparser.controller.request.CSVParserRequest;
import com.codapay.csvparser.controller.response.CSVParserResponse;
import com.codapay.csvparser.service.CSVParserService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.validation.Validated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;

@Controller("/csv-parser")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
@Slf4j
public class CSVParserController {

    private final CSVParserService csvParserService;

    @Post
    @Validated
    public HttpResponse<CSVParserResponse> parseToJson(@Valid @Body CSVParserRequest csvParserRequest) {
        log.info("POST /csv-parser : {}", csvParserRequest);
        return HttpResponse.created(csvParserService.parseToJson(csvParserRequest));
    }

}
