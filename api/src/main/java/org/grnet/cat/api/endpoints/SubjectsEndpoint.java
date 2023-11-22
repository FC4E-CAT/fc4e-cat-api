package org.grnet.cat.api.endpoints;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
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
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.subject.SubjectRequest;
import org.grnet.cat.dtos.subject.SubjectResponse;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.repositories.SubjectRepository;
import org.grnet.cat.services.SubjectService;

@Path("/v1/subjects")
@Authenticated
public class SubjectsEndpoint {

    /**
     * Injection point for the Subject service
     */
    @Inject
    SubjectService subjectService;

    /**
     * Injection point for the Utility class
     */
    @Inject
    Utility utility;

    @ConfigProperty(name = "server.url")
    String serverUrl;

    @Tag(name = "Subject")
    @Operation(
            summary = "Create a new subject.",
            description = "Create a new subject.")
    @APIResponse(
            responseCode = "201",
            description = "Subject created successfully.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = SubjectResponse.class)))
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
            responseCode = "409",
            description = "Subject already exists.",
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
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response create(@Valid @NotNull(message = "The request body is empty.") SubjectRequest request, @Context UriInfo uriInfo) {

        var subject = subjectService.createSubject(request, utility.getUserUniqueIdentifier());

        var serverInfo = new CatServiceUriInfo(serverUrl.concat(uriInfo.getPath()));

        return Response.created(serverInfo.getAbsolutePathBuilder().path(String.valueOf(subject.id)).build()).entity(subject).build();
    }

    @Tag(name = "Subject")
    @Operation(
            summary = "Delete a subject.",
            description = "Deletes a subject if it belongs to the authenticated user.")
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
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response deleteSubject(@Parameter(
            description = "The ID of the subject to be deleted.",
            required = true,
            example = "1",
            schema = @Schema(type = SchemaType.NUMBER)) @PathParam("id")
                                     @Valid @NotFoundEntity(repository = SubjectRepository.class, message = "There is no Subject with the following id:") Long id) {

        subjectService.deleteSubjectBelongsToUser(utility.getUserUniqueIdentifier(), id);

        var informativeResponse = new InformativeResponse();
        informativeResponse.code = 200;
        informativeResponse.message = "Subject has been successfully deleted.";

        return Response.ok().entity(informativeResponse).build();
    }
}
