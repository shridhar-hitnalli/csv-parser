package com.codapay.csvparser.exception;

import com.codapay.csvparser.controller.response.ErrorResponse;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Produces
@Singleton
@Replaces(io.micronaut.validation.exceptions.ConstraintExceptionHandler.class)
@Requires(classes = { ConstraintViolationException.class, ExceptionHandler.class })
public class ConstraintViolationExceptionHandler implements ExceptionHandler<ConstraintViolationException, HttpResponse<ErrorResponse>> {

    @Override
    public HttpResponse<ErrorResponse> handle(HttpRequest request, ConstraintViolationException ex) {

        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        return HttpResponse.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse
                        .builder()
                        .timestamp(LocalDateTime.now())
                        .httpStatusCode(HttpStatus.BAD_REQUEST.getCode())
                        .errors(errors.toArray(new String[]{}))
                        .build()
                );
    }

}
