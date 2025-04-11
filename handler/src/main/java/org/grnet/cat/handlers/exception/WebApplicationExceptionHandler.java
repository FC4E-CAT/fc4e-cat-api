package org.grnet.cat.handlers.exception;

import jakarta.ws.rs.ServerErrorException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.grnet.cat.dtos.InformativeResponse;
import org.jboss.logging.Logger;

@Provider
public class WebApplicationExceptionHandler implements ExceptionMapper<WebApplicationException> {

    private static final Logger LOG = Logger.getLogger(WebApplicationExceptionHandler.class);

    @Override
    public Response toResponse(WebApplicationException e) {

        LOG.error("Web Application Error", e);

        var response = new InformativeResponse();
        response.message = e.getCause().getMessage();
        response.code = e.getResponse().getStatus();

        return Response.status(e.getResponse().getStatus()).entity(response).build();
    }
}
