package org.grnet.cat.handlers.exception;

import io.quarkus.security.ForbiddenException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.grnet.cat.dtos.InformativeResponse;
import org.jboss.logging.Logger;

@Provider
public class AuthorizationForbiddenExceptionHandler implements ExceptionMapper<ForbiddenException> {

    private static final Logger LOG = Logger.getLogger(AuthorizationForbiddenExceptionHandler.class);

    @Override
    public Response toResponse(ForbiddenException e) {

        LOG.error("Authorization Error", e);

        var response = new InformativeResponse();
        response.code = 403;
        response.message = "You do not have permission to access this resource.";

        return Response.status(403).entity(response).build();
    }
}
