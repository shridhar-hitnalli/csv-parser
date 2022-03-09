package com.codapay.csvparser.controller.response;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Introspected
public class CSVParserResponse {

    private List<String> jsonFiles;
}
