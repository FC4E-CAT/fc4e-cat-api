package org.grnet.cat.exceptions;


import jakarta.validation.ValidationException;

public class CustomValidationException extends ValidationException {

    private int code;

    public CustomValidationException(String message, int code){
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
