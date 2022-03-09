package com.codapay.csvparser.exception;

public class BadRequestException extends RuntimeException{

    private static final long serialVersionUID = 3228208641783701155L;

    public BadRequestException() {
        super();
    }

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable t) {
        super(message, t);
    }

    public BadRequestException(Throwable t) {
        super(t);
    }

}
