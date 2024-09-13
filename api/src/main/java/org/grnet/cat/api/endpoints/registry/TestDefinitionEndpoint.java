package org.grnet.cat.api.endpoints.registry;

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
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.test.*;
import org.grnet.cat.repositories.registry.TestDefinitionRepository;
import org.grnet.cat.services.registry.TestDefinitionService;
import org.grnet.cat.utils.Utility;

import java.util.List;

import static org.eclipse.microprofile.openapi.annotations.enums.ParameterIn.QUERY;

@Path("/v1/registry/tests/test-definition")
@Authenticated
public class TestDefinitionEndpoint {
    @Inject
    Utility utility;

    @Inject
    TestDefinitionService testDefinitionService;
    @ConfigProperty(name = "api.server.url")
    String serverUrl;
    @Tag(name = "Test")
    @Operation(
            summary = "Get Test Definition by ID",
            description = "Retrieves a specific Test Definition item by ID."
    )
    @APIResponse(
            responseCode = "200",
            description = "The corresponding Test Definition item.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = TestDefinitionResponseDto.class))
    )
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
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    @Path("/{id}")
    public Response getTestDefinitionById(
            @Parameter(
                    description = "The ID of the Test Definition to retrieve.",
                    required = true,
                    example = "pid_graph:7B428EA4",
                    schema = @Schema(type = SchemaType.STRING))
            @PathParam("id")
            @Valid @NotFoundEntity(repository = TestDefinitionRepository.class, message = "There is no Test Definition with the following id:") String id)
    {
        var response = testDefinitionService.getTestDefinitionById(id);

        return Response.ok(response).build();
    }

    @Tag(name = "Test")
    @Operation(
            summary     = "Create new Test Definition",
            description = "Creates a new Test Definition item."
    )
    @APIResponse(
            responseCode = "201",
            description = "testDefinitionitem created.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = TestDefinitionResponseDto.class))
    )
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
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response createTestDefinition(
            @Valid @NotNull(message = "The request body is empty.") TestDefinitionRequestDto testRequestDto, @Context UriInfo uriInfo) {

        var testDefinition= testDefinitionService.createTestDefinition(utility.getUserUniqueIdentifier(),testRequestDto);
        var serverInfo = new CatServiceUriInfo(serverUrl.concat(uriInfo.getPath()));

        return Response.created(serverInfo.getAbsolutePathBuilder().path(String.valueOf(testDefinition.id)).build()).entity(testDefinition).build();
    }

    @Tag(name = "Test")
    @Operation(
            summary = "Update Test Definition",
            description = "Updates an existing Test Definition item."
    )
    @APIResponse(
            responseCode = "200",
            description = "Subject was updated successfully.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = TestDefinitionResponseDto.class)))
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
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Registration
    @Path("/{id}")
    public Response updateTestDefinition(
            @Parameter(
                    description = "The ID of the Test Definition to update.",
                    required = true,
                    example = "pid_graph:03615660",
                    schema = @Schema(type = SchemaType.STRING))
            @PathParam("id")
            @Valid @NotFoundEntity(repository = TestDefinitionRepository.class, message = "There is no Test Definition with the following id:") String id,
            @Valid TestDefinitionUpdateDto testDefinitionUpdateDto) {

        var updatedDto = testDefinitionService.updateTestDefinition(id, utility.getUserUniqueIdentifier(), testDefinitionUpdateDto);

        return Response.ok(updatedDto).build();
    }

    @Tag(name = "Test")
    @Operation(
            summary = "Delete Test Definition",
            description = "Deletes a specific Test Definition item by ID.")
    @APIResponse(
            responseCode = "200",
            description = "Test Definition item deleted.",
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
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    @Path("/{id}")
    public Response deleteTestDefinition(
            @Parameter(
                    description = "The ID of the Test Definition to be deleted.",
                    required = true,
                    example = "pid_graph:03615660",
                    schema = @Schema(type = SchemaType.STRING))
            @PathParam("id")
            @Valid @NotFoundEntity(repository = TestDefinitionRepository.class, message = "There is no Test Definition with the following id:") String id) {

        var deleted =  testDefinitionService.deleteTestDefinition(id);

        InformativeResponse informativeResponse = new InformativeResponse();

        if (!deleted) {

            informativeResponse.code =500;
            informativeResponse.message = "Test Definition hasn't been deleted. An error occurred.";
        } else {

            informativeResponse.code = 200;
            informativeResponse.message = "Test Definition has been successfully deleted.";
        }

        return Response.ok().entity(informativeResponse).build();

    }

    @Tag(name = "Test")
    @Operation(
            summary = "List all Test Definitions",
            description = "Retrieves a paginated list of Test Definitions."
    )
    @APIResponse(
            responseCode = "200",
            description = "List of TestDefinitions.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = PageableTestDefinitionResponse.class)))
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
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response listTestDefinitions(
            @Parameter(name = "page", in = QUERY,
                    description = "Indicates the page number. Page number must be >= 1.")
            @DefaultValue("1")
            @Min(value = 1, message = "Page number must be >= 1.")
            @QueryParam("page") int page,
            @Parameter(name = "size", in = QUERY,
                    description = "The page size.")
            @DefaultValue("10")
            @Min(value = 1, message = "Page size must be between 1 and 100.")
            @Max(value = 100, message = "Page size must be between 1 and 100.")
            @QueryParam("size") int size,
            @Context UriInfo uriInfo) {

        var testDefinitions = testDefinitionService.getTestDefinitionlistAll(page - 1, size, uriInfo);

        return Response.ok().entity(testDefinitions).build();
    }

    public static class PageableTestDefinitionResponse extends PageResource<TestDefinitionResponseDto> {

        private List<TestDefinitionResponseDto> content;

        @Override
        public List<TestDefinitionResponseDto> getContent() {
            return content;
        }

        @Override
        public void setContent(List<TestDefinitionResponseDto> content) {
            this.content = content;
        }
    }
}
