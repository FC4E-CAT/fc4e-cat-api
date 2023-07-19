package org.grnet.cat.handlers.exception;

import io.quarkus.security.AuthenticationFailedException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.grnet.cat.dtos.InformativeResponse;
import org.jboss.logging.Logger;

@Provider
public class AuthenticationExceptionHandler implements ExceptionMapper<AuthenticationFailedException> {

    private static final Logger LOG = Logger.getLogger(AuthenticationExceptionHandler.class);

    @Override
    public Response toResponse(AuthenticationFailedException e) {

        LOG.error("Authentication Error", e);

        var response = new InformativeResponse();
        response.code = 401;
        response.message = "User has not been authenticated.";

        return Response.status(401).entity(response).build();
    }
}
