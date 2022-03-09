package com.codapay.csvparser.exception;

public class FileNotFoundException extends RuntimeException{

    private static final long serialVersionUID = 5843772667001725959L;

    public FileNotFoundException() {
        super();
    }

    public FileNotFoundException(String message) {
        super(message);
    }

    public FileNotFoundException(String message, Throwable t) {
        super(message, t);
    }

    public FileNotFoundException(Throwable t) {
        super(t);
    }

}
