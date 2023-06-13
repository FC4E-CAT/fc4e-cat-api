package org.grnet.cat.handlers.exception;

import io.quarkus.hibernate.validator.runtime.jaxrs.ResteasyReactiveViolationException;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.grnet.cat.dtos.InformativeResponse;

@Provider
public class ValidationExceptionHandler implements ExceptionMapper<ValidationException> {

    @Override
    public Response toResponse(ValidationException e) {


        InformativeResponse response = new InformativeResponse();

        if(e instanceof ResteasyReactiveViolationException){

            response.message = ((ResteasyReactiveViolationException) e).getConstraintViolations().stream().findFirst().get().getMessageTemplate();
            response.code = Response.Status.BAD_REQUEST.getStatusCode();
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        } else {

            response.message = e.getMessage();
            response.code = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }
}
