package org.grnet.cat.api.endpoints;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.grnet.cat.api.filters.Registration;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.RoleDto;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.services.RoleService;

import java.util.List;

import static org.eclipse.microprofile.openapi.annotations.enums.ParameterIn.QUERY;

@Path("/v1/roles")
@Authenticated
public class RolesEndpoint {

    /**
     * Injection point for the keycloakAdminConnection
     */
    @Inject
    @Named("keycloak-service")
    RoleService roleService;

    @Tag(name = "Roles")
    @Operation(
            summary = "Retrieve available roles for the cat service.",
            description = "This endpoint Retrieves the list of available roles for the service. " +
                    " By default, the first page of 10 Roles will be returned. You can tune the default values by using the query parameters page and size.")
    @APIResponse(
            responseCode = "200",
            description = "List of Roles.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = PageableRole.class)))
    @APIResponse(
            responseCode = "401",
            description = "User has not been authenticated.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @APIResponse(
            responseCode = "403",
            description = "Not permitted.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @APIResponse(
            responseCode = "500",
            description = "Internal Server Error.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @SecurityRequirement(name = "Authentication")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response actorsByPage(@Parameter(name = "page", in = QUERY,
            description = "Indicates the page number. Page number must be >= 1.") @DefaultValue("1") @Min(value = 1, message = "Page number must be >= 1.") @QueryParam("page") int page,
                                 @Parameter(name = "size", in = QUERY,
                                         description = "The page size.") @DefaultValue("10") @Min(value = 1, message = "Page size must be between 1 and 100.")
                                 @Max(value = 100, message = "Page size must be between 1 and 100.") @QueryParam("size") int size,
                                 @Context UriInfo uriInfo) {

        var roles = roleService.getRolesByPage(page-1, size, uriInfo);

        return Response.ok().entity(roles).build();
    }

    public static class PageableRole extends PageResource<RoleDto> {

        private List<RoleDto> content;

        @Override
        public List<RoleDto> getContent() {
            return content;
        }

        @Override
        public void setContent(List<RoleDto> content) {
            this.content = content;
        }
    }
}
