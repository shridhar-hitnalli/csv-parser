package com.codapay.csvparser.service;


import com.codapay.csvparser.controller.request.CSVParserRequest;
import com.codapay.csvparser.controller.response.CSVParserResponse;

public interface CSVParserService {

    CSVParserResponse parseToJson(CSVParserRequest csvParserRequest);
}
