package org.grnet.cat.exceptions;

public class InvalidEnumValueException extends RuntimeException {

    public InvalidEnumValueException(String enumType, String value) {
        super("Invalid value for " + enumType + ": " + value + ". Please provide a valid value.");
    }
}
