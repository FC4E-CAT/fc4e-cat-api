package org.grnet.cat.handlers.exception;


import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.grnet.cat.dtos.InformativeResponse;

@Provider
public class ClientErrorExceptionHandler implements ExceptionMapper<ClientErrorException> {

    @Override
    public Response toResponse(ClientErrorException e) {

        var response = new InformativeResponse();
        response.message = e.getMessage();
        response.code = e.getResponse().getStatus();

        return Response.status(e.getResponse().getStatus()).entity(response).build();
    }
}
