package com.codapay.csvparser.exception;

import com.codapay.csvparser.controller.response.ErrorResponse;
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
@Requires(classes = { FileNotFoundException.class, ExceptionHandler.class })
public class FileNotFoundExceptionHandler implements ExceptionHandler<FileNotFoundException, HttpResponse<ErrorResponse>> {

    @Override
    public HttpResponse<ErrorResponse> handle(HttpRequest request, FileNotFoundException ex) {
        return HttpResponse.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse
                        .builder()
                        .timestamp(LocalDateTime.now())
                        .httpStatusCode(HttpStatus.NOT_FOUND.getCode())
                        .errors(new String[]{ex.getMessage()})
                        .build()
                );
    }

}
