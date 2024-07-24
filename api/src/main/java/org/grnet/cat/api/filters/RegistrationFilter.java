package org.grnet.cat.api.filters;

import jakarta.inject.Inject;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import org.grnet.cat.repositories.UserRepository;
import org.grnet.cat.utils.Utility;

/**
 * This filter intercepts the requests to the endpoints annotated with the {@link Registration Registration} annotation.
 * Only the registered users can access those endpoints. Therefore, it checks whether a user has been registered on the CAT database. If not, it throws a ForbiddenException.
 *
 */
@Registration
@Provider
public class RegistrationFilter implements ContainerRequestFilter {

    @Inject
    UserRepository userRepository;

    @Inject
    Utility utility;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {

        var optionalUser = userRepository.searchByIdOptional(utility.getUserUniqueIdentifier());

        optionalUser.orElseThrow(()-> new ForbiddenException("User has not been registered on CAT service. User registration is a prerequisite for accessing this API resource."));
    }
}
