package org.grnet.cat.exceptions;

/**
 * Exception for handling uniqueness constraint violations.
 * This exception is thrown when a unique constraint is violated,
 * indicating a conflict (HTTP status 409).
 */
public class UniqueConstraintViolationException extends RuntimeException {
    private final int code;

    /**
     * Constructs a new UniqueConstraintViolationException with the specified field name and value.
     * @param fieldName the name of the field that caused the exception.
     * @param fieldValue the value of the field that caused the exception.
     */
    public UniqueConstraintViolationException(String fieldName, String fieldValue) {
        super("The value '" + fieldValue + "' for field '" + fieldName + "' is not unique.");
        this.code = 409;
    }

    /**
     * Returns the HTTP status code for this exception, which is 409 Conflict.
     * @return the HTTP status code for this exception.
     */
    public int getCode() {
        return code;
    }
}
