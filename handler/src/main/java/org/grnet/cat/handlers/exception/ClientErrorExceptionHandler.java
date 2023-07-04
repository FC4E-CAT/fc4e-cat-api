package org.grnet.cat.handlers.exception;


import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.grnet.cat.dtos.InformativeResponse;
import org.jboss.logging.Logger;

@Provider
public class ClientErrorExceptionHandler implements ExceptionMapper<ClientErrorException> {

    private static final Logger LOG = Logger.getLogger(ClientErrorExceptionHandler.class);

    @Override
    public Response toResponse(ClientErrorException e) {

        LOG.error("Client Error", e);

        var response = new InformativeResponse();
        response.message = e.getMessage();
        response.code = e.getResponse().getStatus();

        return Response.status(e.getResponse().getStatus()).entity(response).build();
    }
}
