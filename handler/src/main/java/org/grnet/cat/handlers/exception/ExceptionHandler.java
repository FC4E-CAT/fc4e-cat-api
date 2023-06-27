package org.grnet.cat.handlers.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.grnet.cat.dtos.InformativeResponse;

@Provider
public class ExceptionHandler implements ExceptionMapper<Exception> {


    @Override
    public Response toResponse(Exception e) {

        var response = new InformativeResponse();
        response.message = e.getMessage();
        response.code = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
    }
}
