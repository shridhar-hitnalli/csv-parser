package com.codapay.csvparser.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@lombok.Data
@Builder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CSVData implements Serializable {

    private static final long serialVersionUID = 9140714026264816928L;

    private final List<Field> record = new ArrayList<>();

}
