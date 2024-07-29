package org.grnet.cat.handlers.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.exceptions.UniqueConstraintViolationException;
import org.jboss.logging.Logger;

@Provider
public class UniqueConstraintViolationExceptionHandler implements ExceptionMapper<UniqueConstraintViolationException> {

    @Override
    public Response toResponse(UniqueConstraintViolationException exception) {
        InformativeResponse informativeResponse = new InformativeResponse();
        informativeResponse.code = exception.getCode();
        informativeResponse.message = exception.getMessage();

        return Response.status(Response.Status.CONFLICT)
                .entity(informativeResponse)
                .build();
    }
}
