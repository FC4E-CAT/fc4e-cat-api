package org.grnet.cat.handlers.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.grnet.cat.dtos.InformativeResponse;
import org.jboss.logging.Logger;

@Provider
public class ExceptionHandler implements ExceptionMapper<Exception> {

    private static final Logger LOG = Logger.getLogger(ExceptionHandler.class);

    @Override
    public Response toResponse(Exception e) {

        LOG.error("Internal Server Error", e);

        var response = new InformativeResponse();
        response.message = e.getMessage();
        response.code = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
    }
}
