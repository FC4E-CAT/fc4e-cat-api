package org.grnet.cat.endpoint;

import io.quarkus.oidc.TokenIntrospection;
import io.quarkus.oidc.UserInfo;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.grnet.cat.dto.UserDto;

import java.util.Objects;

@Path("/v1/user")
@Authenticated
public class UserInformation {

    /**
     * Injection point for the User information provided by the OpenID Connect Provider
     */
    @Inject
    UserInfo userInfo;

    /**
     * Injection point for the Token Introspection
     */
    @Inject
    TokenIntrospection tokenIntrospection;

    /**
     * Returns the user information.
     *
     *
     * @return an JSON response containing the user information
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserInfo() {

        var user = UserDto.builder()
                .id(tokenIntrospection.getJsonObject().getString("voperson_id"))
                .email(Objects.isNull(userInfo.getJsonObject().get("email")) ? "": userInfo.getJsonObject().getString("email"))
                .name(Objects.isNull(userInfo.getJsonObject().get("name")) ? "": userInfo.getJsonObject().getString("name"))
                .build();

        return Response.ok().entity(user).build();
    }
}
