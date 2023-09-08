package org.grnet.cat.handlers.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.exceptions.BadRequestException;
import org.jboss.logging.Logger;

@Provider
public class BadRequestExceptionHandler implements ExceptionMapper<BadRequestException> {

    private static final Logger LOG = Logger.getLogger(BadRequestExceptionHandler.class);

    @Override
    public Response toResponse(BadRequestException e) {

        LOG.error("Client Error", e);

        var response = new InformativeResponse();
        response.message = e.getMessage();
        response.code = e.getCode();
        response.errors = e.getErrors();
        return Response.status(e.getCode()).entity(response).build();
    }
}
