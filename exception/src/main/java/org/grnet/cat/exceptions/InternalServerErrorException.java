package org.grnet.cat.exceptions;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.core.Response;

public class InternalServerErrorException extends ClientErrorException {

    public InternalServerErrorException(String message) {
        super(message, Response.Status.INTERNAL_SERVER_ERROR);
    }
}
