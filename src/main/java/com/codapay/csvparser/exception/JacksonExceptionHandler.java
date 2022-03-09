package com.codapay.csvparser.exception;

import com.codapay.csvparser.controller.response.ErrorResponse;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

import java.time.LocalDateTime;

@Produces
@Singleton
@Requires(classes = { ValueInstantiationException.class, ExceptionHandler.class })
public class JacksonExceptionHandler implements ExceptionHandler<ValueInstantiationException, HttpResponse<ErrorResponse>> {

    @Override
    public HttpResponse<ErrorResponse> handle(HttpRequest request, ValueInstantiationException ex) {

        return HttpResponse.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse
                        .builder()
                        .timestamp(LocalDateTime.now())
                        .httpStatusCode(HttpStatus.BAD_REQUEST.getCode())
                        .errors(new String[]{ex.getMessage()})
                        .build()
                );
    }

}
