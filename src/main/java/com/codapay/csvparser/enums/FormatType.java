package com.codapay.csvparser.enums;

import com.codapay.csvparser.exception.BadRequestException;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum FormatType {
    JSON("json"), XML("xml");

    private final String formatType;

    FormatType(String formatType) {
        this.formatType = formatType;
    }

    public String getFormatType() {
        return formatType;
    }

    @JsonCreator
    public static FormatType forValue(String name) {
        switch (name.toLowerCase()) {
            case "json":
                return JSON;
            case "xml":
                return XML;
            default:
                log.warn("Unrecognized format type {} so setting to default json format", name);
                return JSON;
        }
    }
}
