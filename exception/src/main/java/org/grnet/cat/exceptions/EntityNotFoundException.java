
package org.grnet.cat.exceptions;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.core.Response;

public class EntityNotFoundException extends ClientErrorException {

    public EntityNotFoundException( String message) {
        super(message, Response.Status.NOT_FOUND);
    }
}
