package org.grnet.cat.handlers.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.grnet.cat.exceptions.InvalidEnumValueException;
import org.grnet.cat.dtos.InformativeResponse;

@Provider
public class InvalidEnumValueExceptionHandler implements ExceptionMapper<InvalidEnumValueException> {

    @Override
    public Response toResponse(InvalidEnumValueException exception) {
        InformativeResponse response = new InformativeResponse();
        response.code = Response.Status.BAD_REQUEST.getStatusCode();
        response.message = exception.getMessage();

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(response)
                .build();
    }
}
