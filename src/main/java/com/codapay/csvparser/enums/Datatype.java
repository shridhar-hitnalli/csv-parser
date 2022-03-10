package com.codapay.csvparser.enums;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public enum Datatype {
    STRING("^[a-zA-Z]*$"),
    NUMBER("[0-9]+"),
    EMAIL("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$"),
    PHONE_NUMBER("^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$");

    private final String regEx;

    public String getRegEx() {
        return regEx;
    }

    Datatype(String regEx) {
        this.regEx = regEx;
    }

    public static List<Datatype> getTypeOfField(String str) {
        return Arrays.stream(Datatype.values())
            .filter(dt -> Pattern.compile(dt.getRegEx())
                    .matcher(str).matches()).collect(Collectors.toList());
    }
}
