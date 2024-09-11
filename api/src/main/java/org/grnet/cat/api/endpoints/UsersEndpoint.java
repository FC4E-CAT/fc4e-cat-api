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
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.grnet.cat.api.filters.Registration;
import org.grnet.cat.api.utils.CatServiceUriInfo;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.UpdateUserProfileDto;
import org.grnet.cat.dtos.UserAssessmentEligibilityResponse;
import org.grnet.cat.dtos.UserProfileDto;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.services.UserService;
import org.grnet.cat.utils.Utility;

import java.util.List;

import static org.eclipse.microprofile.openapi.annotations.enums.ParameterIn.QUERY;

@Path("/v1/users")
@Authenticated
@SecurityScheme(securitySchemeName = "Authentication",
        description = "JWT token",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT")
public class UsersEndpoint {

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

    @ConfigProperty(name = "api.server.url")
    String serverUrl;

    @Tag(name = "User")
    @Operation(
            summary = "Registers a user in the CAT database.",
            description = "This endpoint creates a new user entity in the database by extracting the voperson_id from the access token and assigning it to the user.")
    @APIResponse(
            responseCode = "201",
            description = "When a user is successfully registered in the database.",
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
    public Response register(@Context UriInfo uriInfo) {

        var serverInfo = new CatServiceUriInfo(serverUrl.concat(uriInfo.getPath()));

        var response = userService.register(utility.getUserUniqueIdentifier());

        return Response.created(serverInfo.getAbsolutePathBuilder().replacePath("/v1/users/profile").build()).entity(response).build();
    }

    @Tag(name = "User")
    @Operation(
            summary = "Get User Profile.",
            description = "This endpoint retrieves the user profile information. User registration is a prerequisite for retrieving user information.")
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
            summary = "Update User Profile Metadata.",
            description = "Updates the metadata for a user's profile. The user can provide their name, surname, and email.")
    @APIResponse(
            responseCode = "200",
            description = "User's metadata updated successfully.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = UserProfileDto.class)))
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
    @Path("/profile")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response updateProfile(@Valid @NotNull(message = "The request body is empty.") UpdateUserProfileDto updateUserProfileDto) {

        var userProfile = userService.updateUserProfileMetadata(utility.getUserUniqueIdentifier(), updateUserProfileDto.name, updateUserProfileDto.surname, updateUserProfileDto.email, updateUserProfileDto.orcidId);

        return Response.ok().entity(userProfile).build();
    }

    @Tag(name = "User")
    @Operation(
            summary = "Get user eligibility for creating new assessments.",
            description = "Returns a structured list of organizations, assessment types, and actors for which the user is eligible to create new assessments.")
    @APIResponse(
            responseCode = "200",
            description = "Successful response.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = PageableAssessmentEligibility.class)))
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
    @Path("/assessment-eligibility")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response userAssessmentEligibility(@Parameter(name = "page", in = QUERY,
            description = "Indicates the page number. Page number must be >= 1.") @DefaultValue("1") @Min(value = 1, message = "Page number must be >= 1.") @QueryParam("page") int page,
                                              @Parameter(name = "size", in = QUERY,
                                                      description = "The page size.") @DefaultValue("10") @Min(value = 1, message = "Page size must be between 1 and 20.")
                                                  @Max(value = 20, message = "Page size must be between 1 and 20.") @QueryParam("size") int size, @Context UriInfo uriInfo) {

        var userProfile = userService.getUserAssessmentEligibility(page-1, size, utility.getUserUniqueIdentifier(), uriInfo);

        return Response.ok().entity(userProfile).build();
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

    public static class PageableAssessmentEligibility extends PageResource<UserAssessmentEligibilityResponse> {

        private List<UserAssessmentEligibilityResponse> content;

        @Override
        public List<UserAssessmentEligibilityResponse> getContent() {
            return content;
        }

        @Override
        public void setContent(List<UserAssessmentEligibilityResponse> content) {
            this.content = content;
        }
    }
}
