package org.grnet.cat.api.endpoints;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
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
import org.grnet.cat.api.utils.Utility;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.constraints.StringEnumeration;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.UpdateValidationStatus;
import org.grnet.cat.dtos.ValidationRequest;
import org.grnet.cat.dtos.ValidationResponse;
import org.grnet.cat.enums.ValidationStatus;
import org.grnet.cat.repositories.ValidationRepository;
import org.grnet.cat.services.ValidationService;

import static org.eclipse.microprofile.openapi.annotations.enums.ParameterIn.QUERY;

@Path("/v1/admin/validations")
@Authenticated
public class AdminValidationsEndpoint {

    /**
     * Injection point for the Validation service
     */
    @Inject
    ValidationService validationService;

    /**
     * Injection point for the Utility service
     */
    @Inject
    Utility utility;

    @Tag(name = "Admin")
    @Operation(
            summary = "Retrieve all validation requests.",
            description = "Retrieves a list of all validations requests submitted by users." +
                    "By default, the first page of 10 validation requests will be returned. You can tune the default values by using the query parameters page and size.")
    @APIResponse(
            responseCode = "200",
            description = "Successful response with the list of validation requests.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = ValidationsEndpoint.PageableValidationResponse.class)))
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
                                @Max(value = 100, message = "Page size must be between 1 and 100.") @QueryParam("size") int size, @Valid @StringEnumeration(enumClass = ValidationStatus.class, message = "status") @QueryParam("status") @DefaultValue("") String status,
                                @Context UriInfo uriInfo) {

        var validations = validationService.getValidationsByPage(page-1, size, status, uriInfo);

        return Response.ok().entity(validations).build();
    }

    @Tag(name = "Admin")
    @Operation(
            summary = "Update the information of a specific validation request.",
            description = "Updates the information of a specific validation request with the provided details.")
    @APIResponse(
            responseCode = "200",
            description = "Validation request was successfully updated.",
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
            description = "Validation request not found.",
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
    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response updateValidation(@Parameter(
            description = "The Validation to be updated.",
            required = true,
            example = "1",
            schema = @Schema(type = SchemaType.NUMBER))
                                         @PathParam("id") @Valid @NotFoundEntity(repository = ValidationRepository.class, message = "There is no Validation with the following id:") Long id,
                                     @Valid @NotNull(message = "The request body is empty.")ValidationRequest validationRequest) {

        var response = validationService.updateValidationRequest(id, validationRequest);

        return Response.ok().entity(response).build();
    }

    @Tag(name = "Admin")
    @Operation(
            summary = "Update the status of a validation request.",
            description = "Updates the status of a validation request with the provided status.")
    @APIResponse(
            responseCode = "200",
            description = "The status of a validation request was successfully updated.",
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
            description = "Validation request not found.",
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
    @PUT
    @Path("/{id}/update-status")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response updateTheStatusOfValidation(@Parameter(
            description = "The Validation to be updated.",
            required = true,
            example = "1",
            schema = @Schema(type = SchemaType.NUMBER))
                                     @PathParam("id") @Valid @NotFoundEntity(repository = ValidationRepository.class, message = "There is no Validation with the following id:") Long id,
                                     @Valid @NotNull(message = "The request body is empty.") UpdateValidationStatus updateValidationStatus) {

        var response  = validationService.updateValidationRequestStatus(id, ValidationStatus.valueOf(updateValidationStatus.status), utility.getUserUniqueIdentifier());

        return Response.ok().entity(response).build();
    }

    @Tag(name = "Admin")
    @Operation(
            summary = "Get Validation Request.",
            description = "Returns a specific validation request.")
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

        var validations = validationService.getValidationRequest(id);

        return Response.ok().entity(validations).build();
    }
}