package org.grnet.cat.api.endpoints;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.grnet.cat.api.filters.Registration;
import org.grnet.cat.api.utils.CatServiceUriInfo;
import org.grnet.cat.api.utils.Utility;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.constraints.StringEnumeration;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.ValidationRequest;
import org.grnet.cat.dtos.ValidationResponse;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.enums.Source;
import org.grnet.cat.enums.ValidationStatus;
import org.grnet.cat.repositories.ValidationRepository;
import org.grnet.cat.services.UserService;
import org.grnet.cat.services.ValidationService;

import java.util.List;

import static org.eclipse.microprofile.openapi.annotations.enums.ParameterIn.QUERY;

@Path("/v1/validations")
@Authenticated
public class ValidationsEndpoint {

    /**
     * Injection point for the Identified service
     */
    @Inject
    UserService userService;

    /**
     * Injection point for the Utility class
     */
    @Inject
    Utility utility;

    /**
     * Injection point for the Validation service
     */
    @Inject
    ValidationService validationService;

    @ConfigProperty(name = "server.url")
    String serverUrl;


    @Tag(name = "Validation")
    @Operation(
            summary = "Request promotion to validated user.",
            description = "This endpoint allows an identified user to request promotion to become a validated user."+
                    " The identified user should provide the necessary information to support their promotion request, which will be reviewed by the administrators.")
    @APIResponse(
            responseCode = "201",
            description = "User promotion request submitted.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = ValidationResponse.class)))
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
            responseCode = "404",
            description = "Entity Not Found.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @APIResponse(
            responseCode = "409",
            description = "Promotion request already exists.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @APIResponse(
            responseCode = "500",
            description = "Internal Server Error.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @APIResponse(
            responseCode = "501",
            description = "Not Implemented.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @SecurityRequirement(name = "Authentication")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response validate(@Valid @NotNull(message = "The request body is empty.") ValidationRequest request, @Context UriInfo uriInfo) {

        var userProfile = userService.getUserProfile(utility.getUserUniqueIdentifier());

        if(StringUtils.isEmpty(userProfile.name) || StringUtils.isEmpty(userProfile.surname) || StringUtils.isEmpty(userProfile.email) ){
            throw new ForbiddenException("You have to update your profile before requesting a validation.");
        }

        Source.valueOf(request.organisationSource).execute(request.organisationId);

        var response = userService.validate(utility.getUserUniqueIdentifier(), request);

        var serverInfo = new CatServiceUriInfo(serverUrl.concat(uriInfo.getPath()));

        return Response.created(serverInfo.getAbsolutePathBuilder().path(String.valueOf(response.id)).build()).entity(response).build();
    }

    @Tag(name = "Validation")
    @Operation(
            summary = "Retrieve validation requests for a specific user.",
            description = "This endpoint retrieves the validation requests submitted by the specified user." +
                    "By default, the first page of 10 validation requests will be returned. You can tune the default values by using the query parameters page and size.")
    @APIResponse(
            responseCode = "200",
            description = "List of validation requests.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = PageableValidationResponse.class)))
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
    public Response validations(@Parameter(name = "page", in = QUERY,
            description = "Indicates the page number. Page number must be >= 1.") @DefaultValue("1") @Min(value = 1, message = "Page number must be >= 1.") @QueryParam("page") int page,
                                @Parameter(name = "size", in = QUERY,
                                        description = "The page size.") @DefaultValue("10") @Min(value = 1, message = "Page size must be between 1 and 100.")
                                @Max(value = 100, message = "Page size must be between 1 and 100.") @QueryParam("size") int size, @Parameter(name = "status", in = QUERY,
            description = "The validation status to filter.") @Valid @StringEnumeration(enumClass = ValidationStatus.class, message = "status") @QueryParam("status") @DefaultValue("") String status,
                                @Context UriInfo uriInfo) {

        var validations = validationService.getValidationsByUserAndPage(page-1, size, status, uriInfo, utility.getUserUniqueIdentifier());

        return Response.ok().entity(validations).build();
    }

    @Tag(name = "Validation")
    @Operation(
            summary = "Get Validation Request.",
            description = "Returns a specific validation request if it belongs to the user.")
    @APIResponse(
            responseCode = "200",
            description = "The corresponding validation request.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = ValidationResponse.class)))
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
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response getValidationRequest(@Parameter(
            description = "The ID of the validation request to retrieve.",
            required = true,
            example = "1",
            schema = @Schema(type = SchemaType.NUMBER)) @PathParam("id")
                                             @Valid @NotFoundEntity(repository = ValidationRepository.class, message = "There is no Validation with the following id:") Long id) {

        var validations = validationService.getValidationRequest(utility.getUserUniqueIdentifier(), id);

        return Response.ok().entity(validations).build();
    }

    public static class PageableValidationResponse extends PageResource<ValidationResponse> {

        private List<ValidationResponse> content;

        @Override
        public List<ValidationResponse> getContent() {
            return content;
        }

        @Override
        public void setContent(List<ValidationResponse> content) {
            this.content = content;
        }
    }
}