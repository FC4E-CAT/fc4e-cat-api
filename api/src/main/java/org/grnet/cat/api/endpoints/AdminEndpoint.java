package org.grnet.cat.api.endpoints;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeIn;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.grnet.cat.api.filters.Registration;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.UpdateValidationStatus;
import org.grnet.cat.dtos.ValidationRequest;
import org.grnet.cat.dtos.ValidationResponse;
import org.grnet.cat.dtos.*;
import org.grnet.cat.dtos.access.DenyAccess;
import org.grnet.cat.dtos.access.PermitAccess;
import org.grnet.cat.dtos.assessment.AdminPartialJsonAssessmentResponse;
import org.grnet.cat.dtos.assessment.registry.JsonRegistryAssessmentRequest;
import org.grnet.cat.dtos.assessment.registry.UserJsonRegistryAssessmentResponse;
import org.grnet.cat.dtos.assessment.zenodo.ZenodoDepositResponse;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.statistics.StatisticsResponse;
import org.grnet.cat.enums.ValidationStatus;
import org.grnet.cat.repositories.*;
import org.grnet.cat.services.*;
import org.grnet.cat.services.assessment.JsonAssessmentService;
import org.grnet.cat.services.registry.RegistryActorService;
import org.grnet.cat.services.zenodo.ZenodoService;
import org.grnet.cat.utils.Utility;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.eclipse.microprofile.openapi.annotations.enums.ParameterIn.QUERY;

@Path("/v1/admin")
@Authenticated
@SecurityScheme(securitySchemeName = "Authentication",
        description = "JWT token",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER)
public class AdminEndpoint {

    /**
     * Injection point for the Validation service
     */
    @Inject
    ValidationService validationService;

    /**
     * Injection point for the JsonAssessment service
     */
    @Inject
    JsonAssessmentService assessmentService;

    /**
     * Injection point for the User Service
     */
    @Inject
    UserService userService;

    /**
     * Injection point for the Actor Service
     */
    @Inject
    RegistryActorService actorService;

    /**
     * Injection point for the KeycloakAdminRole Service
     */
    @Inject
    KeycloakAdminRoleService adminService;
    /**
     * Injection point for the Zenodo Service
     */
    @Inject
    ZenodoService zenodoService;

    /**
     * Injection point for the Utility service
     */

    @Inject
    Utility utility;


    @Tag(name = "Admin")
    @Operation(
            summary = "Retrieve all validation requests.",
            description = "Retrieves a list of all validations requests submitted by users." +
                    "By default, the first page of 10 validation requests will be returned ordered by the date created. You can tune the default values by using the query parameters page and size.")
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
    @Path("/validations")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response validations(@Parameter(name = "page", in = QUERY,
            description = "Indicates the page number. Page number must be >= 1.") @DefaultValue("1") @Min(value = 1, message = "Page number must be >= 1.") @QueryParam("page") int page,
                                @Parameter(name = "size", in = QUERY,
                                        description = "The page size.") @DefaultValue("10") @Min(value = 1, message = "Page size must be between 1 and 100.")
                                @Max(value = 100, message = "Page size must be between 1 and 100.") @QueryParam("size") int size,
                                @Parameter(name = "search", in = QUERY,
                                        description = "The \"search\" parameter is a query parameter that allows clients to specify a text string that will be used to search for matches in specific fields in Validation entity. The search will be conducted in the following fields : id, organization's name, user's name.") @QueryParam("search") String search,
                                @Parameter(name = "sort", in = QUERY,
                                        schema = @Schema(type = SchemaType.STRING, defaultValue = ""),
                                        examples = {@ExampleObject(name = "Organization name", value = "organisationName"), @ExampleObject(name = "Created On", value = "createdOn"), @ExampleObject(name = "Status", value = "status")},
                                        description = "The \"sort\" parameter allows clients to specify the field by which they want the results to be sorted.") @DefaultValue("") @QueryParam("sort") String sort,
                                @Parameter(name = "order",
                                        in = QUERY,
                                        schema = @Schema(type = SchemaType.STRING, defaultValue = ""),
                                        examples = {@ExampleObject(name = "Ascending", value = "ASC"), @ExampleObject(name = "Descending", value = "DESC")},
                                        description = "The \"order\" parameter specifies the order in which the sorted results should be returned.") @DefaultValue("DESC") @QueryParam("order") String order,
                                @Parameter(name = "status",
                                        in = QUERY,
                                        schema = @Schema(type = SchemaType.STRING, defaultValue = ""),
                                        examples = {@ExampleObject(name = "Approved", value = "APPROVED"), @ExampleObject(name = "Pending", value = "PENDING"), @ExampleObject(name = "Review", value = "REVIEW"), @ExampleObject(name = "Rejected", value = "REJECTED")},
                                        description = "The \"status\" parameter allows clients to filter the results based on the status of the validation.") @QueryParam("status") String status,
                                @Parameter(name = "type",
                                        in = QUERY,
                                        description = "The \"type\" parameter allows clients to filter the results based on the type of actor.") @QueryParam("type") String type,
                                @Context UriInfo uriInfo) {

        var orderValues = List.of("ASC", "DESC");
        var sortValues = List.of("organisationName", "createdOn", "", "status");

        if (!orderValues.contains(order)) {

            throw new BadRequestException("The available values of order parameter are : " + orderValues);
        }

        if (!sortValues.contains(sort)) {

            throw new BadRequestException("The available values of sort parameter are : " + sortValues);
        }

        if (status != null && !Arrays.stream(ValidationStatus.values()).map(Enum::name).collect(Collectors.toList()).contains(status)) {

            throw new BadRequestException("The value " + status + " is not a valid status. Valid status values are: " + Arrays.toString(ValidationStatus.values()));
        }

        if (type != null) {

            actorService.doesActorWithGivenNameExist(type);
        }

        var validations = validationService.getValidationsByPage(search, sort, order, type, status, page - 1, size, uriInfo);

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
    @Path("/validations/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response updateValidation(@Parameter(
            description = "The Validation to be updated.",
            required = true,
            example = "1",
            schema = @Schema(type = SchemaType.NUMBER))
                                     @PathParam("id") @Valid @NotFoundEntity(repository = ValidationRepository.class, message = "There is no Validation with the following id:") Long id,
                                     @Valid @NotNull(message = "The request body is empty.") ValidationRequest validationRequest) {

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
    @Path("/validations/{id}/update-status")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response updateTheStatusOfValidation(@Parameter(
            description = "The Validation to be updated.",
            required = true,
            example = "1",
            schema = @Schema(type = SchemaType.NUMBER))
                                                    @PathParam("id") @Valid @NotFoundEntity(repository = ValidationRepository.class, message = "There is no Validation with the following id:") Long id,
                                                    @Valid @NotNull(message = "The request body is empty.") UpdateValidationStatus updateValidationStatus) {

        var response = validationService.updateValidationRequestStatus(id, ValidationStatus.valueOf(updateValidationStatus.status), utility.getUserUniqueIdentifier(), updateValidationStatus.rejectionReason);

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
    @Path("/validations/{id}")
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

    @Tag(name = "Admin")
    @Operation(
            summary = "Delete private Assessment.",
            description = "Deletes a private assessment if it is not published.")
    @APIResponse(
            responseCode = "200",
            description = "Deletion completed.",
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
            responseCode = "500",
            description = "Internal Server Error.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @SecurityRequirement(name = "Authentication")
    @DELETE
    @Path("/assessments/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response deleteAssessment(@Parameter(
            description = "The ID of the assessment to be deleted.",
            required = true,
            example = "c242e43f-9869-4fb0-b881-631bc5746ec0",
            schema = @Schema(type = SchemaType.STRING)) @PathParam("id")
                                     @Valid @NotFoundEntity(repository = MotivationAssessmentRepository.class, message = "There is no Assessment with the following id:") String id) {

        assessmentService.delete(id);

        var informativeResponse = new InformativeResponse();
        informativeResponse.code = 200;
        informativeResponse.message = "Assessment has been successfully deleted.";

        return Response.ok().entity(informativeResponse).build();
    }

    @Tag(name = "Admin")
    @Operation(
            summary = "Retrieve a list of available users.",
            description = "This endpoint returns a list of users registered in the service. Each user object includes basic information such as their type and unique id. " +
                    " By default, the first page of 10 Users will be returned. You can tune the default values by using the query parameters page and size.")
    @APIResponse(
            responseCode = "200",
            description = "List of Users.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = UsersEndpoint.PageableUserProfile.class)))
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
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response usersByPage(@Parameter(name = "page", in = QUERY,
            description = "Indicates the page number. Page number must be >= 1.") @DefaultValue("1") @Min(value = 1, message = "Page number must be >= 1.") @QueryParam("page") int page,
                                @Parameter(name = "size", in = QUERY,
                                        description = "The page size.") @DefaultValue("10") @Min(value = 1, message = "Page size must be between 1 and 20.")
                                @Max(value = 20, message = "Page size must be between 1 and 20.") @QueryParam("size") int size,
                                @Parameter(name = "search", in = QUERY,
                                        description = "The \"search\" parameter is a query parameter that allows clients to specify a text string that will be used to search for matches in specific fields in User entity. The search will be conducted in the following fields : id, name, surname, email, orcid_id.") @QueryParam("search") String search,
                                @Parameter(name = "sort", in = QUERY,
                                        schema = @Schema(type = SchemaType.STRING, defaultValue = "id"),
                                        examples = {@ExampleObject(name = "id", value = "id"), @ExampleObject(name = "name", value = "name"), @ExampleObject(name = "surname", value = "surname"), @ExampleObject(name = "email", value = "email"), @ExampleObject(name = "orcid id", value = "orcidId")},
                                        description = "The \"sort\" parameter allows clients to specify the field by which they want the results to be sorted.") @DefaultValue("id") @QueryParam("sort") String sort,
                                @Parameter(name = "order",
                                        in = QUERY,
                                        schema = @Schema(type = SchemaType.STRING, defaultValue = "ASC"),
                                        examples = {@ExampleObject(name = "Ascending", value = "ASC"), @ExampleObject(name = "Descending", value = "DESC")},
                                        description = "The \"order\" parameter specifies the order in which the sorted results should be returned.") @DefaultValue("ASC") @QueryParam("order") String order,
                                @Parameter(name = "status",
                                        in = QUERY,
                                        schema = @Schema(type = SchemaType.STRING, defaultValue = ""),
                                        examples = {@ExampleObject(name = "Active", value = "active"), @ExampleObject(name = "Deleted", value = "deleted")},
                                        description = "The \"status\" parameter allows clients to filter the results based on the status of the user, indicating whether the user is active or deleted.") @QueryParam("status") String status,
                                @Parameter(name = "type",
                                        in = QUERY,
                                        schema = @Schema(type = SchemaType.STRING, defaultValue = ""),
                                        examples = {@ExampleObject(name = "Admin", value = "Admin"), @ExampleObject(name = "Identified", value = "Identified"), @ExampleObject(name = "Validated", value = "Validated")},
                                        description = "The \"type\" parameter allows clients to filter the results based on the type of user.") @QueryParam("type") String type,
                                @Context UriInfo uriInfo) {


        var statusValues = List.of("active", "deleted");
        var orderValues = List.of("ASC", "DESC");
        var sortValues = List.of("id", "name", "surname", "email", "orcidId");
        var typeValues = List.of("Admin", "Identified", "Validated");

        if (!orderValues.contains(order)) {

            throw new BadRequestException("The available values of order parameter are : " + orderValues);
        }

        if (!sortValues.contains(sort)) {

            throw new BadRequestException("The available values of sort parameter are : " + sortValues);
        }

        if (status != null && !statusValues.contains(status)) {

            throw new BadRequestException("The available values of status parameter are : " + statusValues);
        }

        if (type != null && !typeValues.contains(type)) {

            throw new BadRequestException("The available values of type parameter are : " + typeValues);
        }

        var userProfile = userService.getUsersByPage(search, sort, order, status, type, page - 1, size, uriInfo);

        return Response.ok().entity(userProfile).build();
    }

    @Tag(name = "Admin")
    @Operation(
            summary = "Retrieve a user's information .",
            description = "This endpoint returns the profile of a registered user in the service.")
    @APIResponse(
            responseCode = "200",
            description = "User's information",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = UserInfoDto.class)))
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
    @Path("/users/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserInfo(@Parameter(description = "The ID of the user to retrieve his validation requests.", required = true, schema = @Schema(name = "UserProfile"))
                                @PathParam("id") @Valid @NotFoundEntity(repository = UserRepository.class, message = "There is no User with the following id:") String id){

        var userInfo = userService.getUserStatsById(id);

        return Response.ok(userInfo).build();
    }

    @Tag(name = "Admin")
    @Operation(
            summary = "Restrict a user's access.",
            description = "Calling this endpoint results in the specified user being denied access to the API.")
    @APIResponse(
            responseCode = "200",
            description = "Successful operation.",
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
    @PUT
    @Path("/users/deny-access")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response denyAccess(@Valid @NotNull(message = "The request body is empty.") DenyAccess denyAccess) {

        userService.addDenyAccessRole(utility.getUserUniqueIdentifier(), denyAccess.userId, denyAccess.reason);

        var informativeResponse = new InformativeResponse();
        informativeResponse.code = 200;
        informativeResponse.message = "deny_access role added successfully to the user. The user is now denied access to the API.";

        return Response.ok().entity(informativeResponse).build();
    }

    @Tag(name = "Admin")
    @Operation(
            summary = "Allow Access to previously banned user.",
            description = "Executing this endpoint allows a user who has been previously banned to access the CAT Service again.")
    @APIResponse(
            responseCode = "200",
            description = "Successful operation.",
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
    @PUT
    @Path("/users/permit-access")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response permitAccess(@Valid @NotNull(message = "The request body is empty.") PermitAccess permitAccess) {

        userService.removeDenyAccessRole(utility.getUserUniqueIdentifier(), permitAccess.userId, permitAccess.reason);

        var informativeResponse = new InformativeResponse();
        informativeResponse.code = 200;
        informativeResponse.message = "deny_access role removed successfully from the user. The user is now allowed access to the API.";

        return Response.ok().entity(informativeResponse).build();
    }

    @Tag(name = "Admin")
    @Operation(
            summary = "Get  Statistics Results.",
            description = "Returns results of Statistics.")
    @APIResponse(
            responseCode = "200",
            description = "The corresponding Assessment Statistics request.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = StatisticsResponse.class)))
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
    @Path("/statistics")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response getStatisticsRequest() {

        var statistics = adminService.getStatistics();

        return Response.ok().entity(statistics).build();
    }

    @Tag(name = "Admin")
    @Operation(
            summary = "Update an existing assessment.",
            description = "Allows an admin to update the details of an existing assessment.")
    @APIResponse(
            responseCode = "200",
            description = "Assessment updated successfully.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = UserJsonRegistryAssessmentResponse.class)))
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
            description = "Assessment not found.",
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
    @Path("/assessments/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response updateAssessment(@Parameter(
            description = "The ID of the assessment to be updated.",
            required = true,
            example = "c242e43f-9869-4fb0-b881-631bc5746ec0",
            schema = @Schema(type = SchemaType.STRING))
                                     @Valid @NotFoundEntity(repository = MotivationAssessmentRepository.class, message = "There is no assessment with the following id:") String id,
                                     @Valid @NotNull(message = "The request body is empty.") JsonRegistryAssessmentRequest request) {

        var response = assessmentService.update(id, request);

        return Response.ok().entity(response).build();
    }

    @Tag(name = "Admin")
    @Operation(
            summary = "Retrieve all assessments.",
            description = "Retrieves a list of all assessments submitted by users." +
                    "By default, the first page of 10 assessments will be returned ordered by the date created. You can tune the default values by using the query parameters page and size.")
    @APIResponse(
            responseCode = "200",
            description = "Successful response with the list of assessments.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = PageablePartialAssessmentResponse.class)))
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
    @Path("/assessments")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response assessments(@Parameter(name = "page", in = QUERY, description = "Indicates the page number. Page number must be >= 1.") @DefaultValue("1") @Min(value = 1, message = "Page number must be >= 1.") @QueryParam("page") int page,
                                @Parameter(name = "size", in = QUERY, description = "The page size.") @DefaultValue("10") @Min(value = 1, message = "Page size must be between 1 and 100.") @Max(value = 100, message = "Page size must be between 1 and 100.") @QueryParam("size") int size,
                                @Parameter(name = "search", in = QUERY, description = "Search term for user's email or user's name or assessment ID") @QueryParam("search") String search,
                                @Context UriInfo uriInfo) {

        var assessments = assessmentService.getAllAssessmentsByPage(page - 1, size, search, uriInfo);

        return Response.ok().entity(assessments).build();
    }

    @Tag(name = "Admin")
    @Operation(
            summary = "Get Assessment.",
            description = "Returns a specific assessment.")
    @APIResponse(
            responseCode = "200",
            description = "The corresponding assessment.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = UserJsonRegistryAssessmentResponse.class)))
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
            responseCode = "500",
            description = "Internal Server Error.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @SecurityRequirement(name = "Authentication")
    @GET
    @Path("/assessments/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    @Registration
    public Response getAssessment(@Parameter(
            description = "The ID of the assessment to retrieve.",
            required = true,
            example = "c242e43f-9869-4fb0-b881-631bc5746ec0",
            schema = @Schema(type = SchemaType.STRING)) @PathParam("id")
                                  @Valid @NotFoundEntity(repository = MotivationAssessmentRepository.class, message = "There is no Assessment with the following id:") String id) {

        var assessment = assessmentService.getRegistryDtoAssessment(id);

        return Response.ok().entity(assessment).build();
    }

    public static class PageablePartialAssessmentResponse extends PageResource<AdminPartialJsonAssessmentResponse> {

        private List<AdminPartialJsonAssessmentResponse> content;

        @Override
        public List<AdminPartialJsonAssessmentResponse> getContent() {
            return content;
        }

        @Override
        public void setContent(List<AdminPartialJsonAssessmentResponse> content) {
            this.content = content;
        }
    }

    @Tag(name = "Admin")
    @Operation(
            summary = "Publish an existing assessment.",
            description = "Allows an admin to publish an existing assessment.")
    @APIResponse(
            responseCode = "200",
            description = "Assessment published successfully.",
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
            description = "Not permitted.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @APIResponse(
            responseCode = "404",
            description = "Assessment not found.",
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
    @Path("/assessments/{id}/publish")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response publishAssessment(@Parameter(
                                             description = "The ID of the assessment to be updated.",
                                             required = true,
                                             example = "c242e43f-9869-4fb0-b881-631bc5746ec0",
                                             schema = @Schema(type = SchemaType.STRING))
                                     @Valid @NotFoundEntity(repository = MotivationAssessmentRepository.class, message = "There is no assessment with the following id:") String id) {

        var message = assessmentService.managePublishAssessmentByAdmin(id,true);
        var response=new InformativeResponse();
        response.code=200;
        response.message=message;
        return Response.ok().entity(response).build();
    }

    @Tag(name = "Admin")
    @Operation(
            summary = "Unpublish an existing assessment.",
            description = "Allows an admin to unpublish an existing assessment.")
    @APIResponse(
            responseCode = "200",
            description = "Assessment unpublished successfully.",
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
            description = "Not permitted.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @APIResponse(
            responseCode = "404",
            description = "Assessment not found.",
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
    @Path("/assessments/{id}/unpublish")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response unpublishAssessment(@Parameter(
                                              description = "The ID of the assessment to be updated.",
                                              required = true,
                                              example = "c242e43f-9869-4fb0-b881-631bc5746ec0",
                                              schema = @Schema(type = SchemaType.STRING))
                                      @Valid @NotFoundEntity(repository = MotivationAssessmentRepository.class, message = "There is no assessment with the following id:") String id) {

        var message = assessmentService.managePublishAssessmentByAdmin(id,false);
        var response=new InformativeResponse();
        response.code=200;
        response.message=message;
        return Response.ok().entity(response).build();
    }

    @Tag(name = "Admin")
    @Operation(
            summary = "Get a Zenodo  assessment.",
            description = "Allows an admin to view information of an assessment in zenodo, as it is stored to CAT service.")
    @APIResponse(
            responseCode = "200",
            description = "Information of zenodo assessment, in CAT service, displayed successfully.",
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
            description = "Not permitted.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @APIResponse(
            responseCode = "404",
            description = "Assessment not found.",
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
    @Path("/zenodo/assessment/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON) // This consumes binary data
    @Registration
    public Response getZenodoAssessment(@Parameter(
            description = "The ID of the assessment uploaded to zenodo.",
            required = true,
            example = "c242e43f-9869-4fb0-b881-631bc5746ec0",
            schema = @Schema(type = SchemaType.STRING)) @PathParam("id")
                                        @Valid @NotFoundEntity(repository = MotivationAssessmentRepository.class, message = "There is no Assessment with the following id:") String assessmentId) throws IOException, InterruptedException {

        var response = zenodoService.getAdminAssessment(assessmentId);
        return Response.ok().entity(response).build();
    }

    @Tag(name = "Admin")
    @Operation(
            summary = "Get Info about a deposit in Zenodo.",
            description = "Allows an admin to view deposit info  existing in zenodo, as the info exist in the CAT service.")
    @APIResponse(
            responseCode = "200",
            description = "Zenodo deposit information displayed successfully.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = ZenodoDepositResponse.class)))
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
            description = "Assessment not found.",
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
    @Path("/zenodo/deposit/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
     @Registration
    public Response getZenodoDeposit(@Parameter(
            description = "The ID of the deposit existing in zenodo.",
            required = true,
            example = "13467",
            schema = @Schema(type = SchemaType.STRING)) @PathParam("id")
                                     String depositId) {
        var response = zenodoService.getAdminDeposit(depositId);
        return Response.ok().entity(response).build();
    }

}