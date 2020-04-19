package com.example.apps.mega.exception;

public class InvalidFieldException extends RuntimeException {
    private String fieldName;

    public InvalidFieldException(String fieldName, String message) {
        super(message);
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
