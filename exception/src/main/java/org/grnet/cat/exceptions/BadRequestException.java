package org.grnet.cat.exceptions;

import java.util.Set;

public class BadRequestException extends RuntimeException{

    private int code = 400;
    private Set<String> errors;

    public BadRequestException(String message, Set<String> errors){

        super(message);
        this.errors = errors;
    }

    public int getCode() {
        return code;
    }

    public Set<String> getErrors() {
        return errors;
    }
}
