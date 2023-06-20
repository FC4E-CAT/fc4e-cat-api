package org.grnet.cat.api.endpoints;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeIn;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.grnet.cat.api.filters.Registration;
import org.grnet.cat.api.utils.Utility;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.UpdateUserProfileDto;
import org.grnet.cat.dtos.UserProfileDto;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.services.IdentifiedService;
import org.grnet.cat.services.UserService;

import java.util.List;

import static org.eclipse.microprofile.openapi.annotations.enums.ParameterIn.QUERY;

@Path("/v1/users")
@Authenticated
@SecurityScheme(securitySchemeName = "Authentication",
        description = "JWT token",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER)
public class UsersEndpoint {

    /**
     * Injection point for the Identified Service
     */
    @Inject
    IdentifiedService identifiedService;

    /**
     * Injection point for the User Service
     */
    @Inject
    UserService userService;

    /**
     * Injection point for the Utility class
     */
    @Inject
    Utility utility;

    @Tag(name = "User")
    @Operation(
            summary = "Registers a user in the CAT database.",
            description = "This endpoint creates a new user entity in the database by extracting the voperson_id from the access token and assigning it to the user.")
    @APIResponse(
            responseCode = "200",
            description = "When a user is successfully registered in the database.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @APIResponse(
            responseCode = "401",
            description = "User has not been authenticated.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @APIResponse(
            responseCode = "409",
            description = "User already exists in the database.",
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
    @Path("/register")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response register() {

        identifiedService.register(utility.getUserUniqueIdentifier());

        var response = new InformativeResponse();
        response.code = 200;
        response.message = "User has been successfully registered on Cat Service.";

        return Response.ok().entity(response).build();
    }

    @Tag(name = "User")
    @Operation(
            summary = "Get User Profile.",
            description = "This endpoint retrieves the user profile information. User registration is a prerequisite for retrieving user information.")
    @SecurityScheme
    @APIResponse(
            responseCode = "200",
            description = "User's Profile.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = UserProfileDto.class)))
    @APIResponse(
            responseCode = "401",
            description = "User has not been authenticated.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @APIResponse(
            responseCode = "403",
            description = "User has not been registered on CAT service.",
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
    @Path("/profile")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response profile() {

        var userProfile = userService.getUserProfile(utility.getUserUniqueIdentifier());

        return Response.ok().entity(userProfile).build();
    }

    @Tag(name = "User")
    @Operation(
            summary = "Retrieve a list of available users.",
            description = "This endpoint returns a list of users registered in the service. Each user object includes basic information such as their type and unique id. " +
                    " By default, the first page of 10 Users will be returned. You can tune the default values by using the query parameters page and size.")
    @SecurityScheme
    @APIResponse(
            responseCode = "200",
            description = "List of Users.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = PageableUserProfile.class)))
    @APIResponse(
            responseCode = "401",
            description = "User has not been authenticated.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @APIResponse(
            responseCode = "403",
            description = "User has not been registered on CAT service.",
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
    public Response usersByPage(@Parameter(name = "page", in = QUERY,
            description = "Indicates the page number. Page number must be >= 1.") @DefaultValue("1") @Min(value = 1, message = "Page number must be >= 1.") @QueryParam("page") int page,
                                            @Parameter(name = "size", in = QUERY,
                                                    description = "The page size.") @DefaultValue("10") @Min(value = 1, message = "Page size must be between 1 and 100.")
                                                @Max(value = 100, message = "Page size must be between 1 and 100.") @QueryParam("size") int size,
                                            @Context UriInfo uriInfo) {

        var userProfile = userService.getUsersByPage(page-1, size, uriInfo);

        return Response.ok().entity(userProfile).build();
    }

    @Tag(name = "User")
    @Operation(
            summary = "Update User Profile Metadata.",
            description = "Updates the metadata for a user's profile. The user can provide their name, surname, and email.")
    @SecurityScheme
    @APIResponse(
            responseCode = "200",
            description = "User's metadata updated successfully.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @APIResponse(
            responseCode = "400",
            description = "Invalid request payload.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @APIResponse(
            responseCode = "401",
            description = "User has not been authenticated.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @APIResponse(
            responseCode = "403",
            description = "User has not been registered on CAT service.",
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
    @Path("/profile")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response updateProfile(@Valid @NotNull(message = "The request body is empty.") UpdateUserProfileDto updateUserProfileDto) {

        userService.updateUserProfileMetadata(utility.getUserUniqueIdentifier(), updateUserProfileDto.name, updateUserProfileDto.surname, updateUserProfileDto.email);

        var response = new InformativeResponse();
        response.code = 200;
        response.message = "User's metadata updated successfully.";

        return Response.ok().entity(response).build();
    }

    public static class PageableUserProfile extends PageResource<UserProfileDto> {

        private List<UserProfileDto> content;

        @Override
        public List<UserProfileDto> getContent() {
            return content;
        }

        @Override
        public void setContent(List<UserProfileDto> content) {
            this.content = content;
        }
    }
}
