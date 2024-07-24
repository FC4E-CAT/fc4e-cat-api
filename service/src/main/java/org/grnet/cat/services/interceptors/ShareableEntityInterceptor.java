package org.grnet.cat.services.interceptors;

import io.quarkus.arc.ArcInvocationContext;
import io.quarkus.security.ForbiddenException;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.grnet.cat.enums.ShareableEntityType;
import org.grnet.cat.repositories.AssessmentRepository;
import org.grnet.cat.services.KeycloakAdminService;
import org.grnet.cat.services.utils.Utility;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

@ShareableEntity(type = ShareableEntityType.ASSESSMENT, id = String.class)
@Interceptor
@Priority(3000)
/**
 * Interceptor for managing entities based on specified criteria. This interceptor is designed to be used with methods annotated
 * with {@link ShareableEntity}. It is intended to work when the first parameter of the intercepted method is expected to be an argument representing an entity ID.
 * It verifies if the current user has permission to manage the entity.
 */

public class ShareableEntityInterceptor {

    @Inject
    Utility utility;

    @Inject
    KeycloakAdminService keycloakAdminService;

    @ConfigProperty(name = "quarkus.oidc.client-id")
    @Getter
    @Setter
    private String clientID;

    @Inject
    AssessmentRepository assessmentRepository;

    @AroundInvoke
    Object canManageTheEntity(InvocationContext context) throws Exception {

        var args = context.getParameters();

        var shareableEntity = Stream
                .of(context.getContextData().get(ArcInvocationContext.KEY_INTERCEPTOR_BINDINGS))
                .map(annotations-> (Set<Annotation>) annotations)
                .flatMap(Collection::stream)
                .filter(annotation -> annotation.annotationType().equals(ShareableEntity.class))
                .map(annotation -> (ShareableEntity) annotation)
                .findFirst()
                .get();

        if(args.length > 0 && shareableEntity.id().isInstance(args[0])){

            if(String.class.isAssignableFrom(shareableEntity.id())){

                var id = (String) args[0];

                //TODO This functionality has to be removed at some point. We keep it because there are assessments that have been created before adding the keycloak user's attribute mechanism.
                var assessment = assessmentRepository.findById(id);

                if (assessment.getValidation().getUser().getId().equals(utility.getUserUniqueIdentifier())) {

                    return context.proceed();
                }

                keycloakAdminService
                        .getUserEntitlements(utility.getUserUniqueIdentifier())
                        .stream()
                        .filter(entitlement->entitlement.equals(shareableEntity.type().getValue().concat(":").concat(id)))
                        .findAny()
                        .orElseThrow(()->new ForbiddenException("You do not have permission to access this resource."));

                return context.proceed();

            } else if (Long.class.isAssignableFrom(shareableEntity.id())) {

                var id = (Long) args[0];

                keycloakAdminService
                        .getUserEntitlements(utility.getUserUniqueIdentifier())
                        .stream()
                        .filter(entitlement->entitlement.equals(shareableEntity.type().getValue().concat(":").concat(id.toString())))
                        .findAny()
                        .orElseThrow(()->new ForbiddenException("You do not have permission to access this resource."));
            } else{

                return context.proceed();
            }
        }

        return context.proceed();
    }
}