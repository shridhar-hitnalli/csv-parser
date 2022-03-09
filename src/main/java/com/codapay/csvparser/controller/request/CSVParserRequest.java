package com.codapay.csvparser.controller.request;

import com.codapay.csvparser.enums.FormatType;
import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Introspected
public class CSVParserRequest {

    @NotBlank(message = "path can not be blank")
    private String path;

    @Builder.Default
    private FormatType format = FormatType.JSON;
}
