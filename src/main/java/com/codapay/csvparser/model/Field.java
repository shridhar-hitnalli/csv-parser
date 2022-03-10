package com.codapay.csvparser.model;

import com.codapay.csvparser.enums.Datatype;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Field implements Serializable {

    private static final long serialVersionUID = 2300434550131351849L;

    private String fieldName;

    private String value;

    private Datatype datatype;

}
