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
import org.grnet.cat.repositories.registry.TestRepository;

import org.grnet.cat.services.registry.TestService;
import org.grnet.cat.utils.Utility;

import java.util.List;

import static org.eclipse.microprofile.openapi.annotations.enums.ParameterIn.QUERY;

@Path("/v1/registry/tests")
@Authenticated
public class TestEndpoint {
    @Inject
    Utility utility;

    @Inject
    TestService testService;

    @ConfigProperty(name = "api.server.url")
    String serverUrl;


    @Tag(name = "Test")
    @Operation(
            summary = "Get Test by ID",
            description = "Retrieves a specific Test item by ID."
    )
    @APIResponse(
            responseCode = "200",
            description = "The corresponding Test item.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = TestAndTestDefinitionResponse.class))
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
    public Response getTestById(
            @Parameter(
                    description = "The ID of the Test to retrieve.",
                    required = true,
                    example = "pid_graph:9F1A6267",
                    schema = @Schema(type = SchemaType.STRING))
            @PathParam("id")
            @Valid @NotFoundEntity(repository = TestRepository.class, message = "There is no Test with the following id:") String id) {

        var response = testService.getTestAndTestDefinitionById(id);

        return Response.ok(response).build();
    }

    @Tag(name = "Test")
    @Operation(
            summary = "Create new Test",
            description = "Creates a new Test item."
    )
    @APIResponse(
            responseCode = "201",
            description = "Test item created.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = TestResponseDto.class))
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
    public Response createTest(
            @Valid @NotNull(message = "The request body is empty.") TestAndTestDefinitionRequest request, @Context UriInfo uriInfo) {

        var test = testService.createTestAndTestDefinition(utility.getUserUniqueIdentifier(), request);
        var serverInfo = new CatServiceUriInfo(serverUrl.concat(uriInfo.getPath()));

        return Response.created(serverInfo.getAbsolutePathBuilder().path(String.valueOf(test.testResponse.id)).build()).entity(test).build();
    }

    @Tag(name = "Test")
    @Operation(
            summary = "Update Test",
            description = "Updates an existing Test item."
    )
    @APIResponse(
            responseCode = "200",
            description = "Subject was updated successfully.",
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
    @PATCH
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Registration
    @Path("/{id}")
    public Response updateTest(
            @Parameter(
                    description = "The ID of the Test to update.",
                    required = true,
                    example = "pid_graph:9F1A6267",
                    schema = @Schema(type = SchemaType.STRING))
            @PathParam("id")
            @Valid @NotFoundEntity(repository = TestRepository.class, message = "There is no Test with the following id:") String id,
            @Valid TestAndTestDefinitionUpdateRequest testUpdateDto) {

        testService.updateTest(id, utility.getUserUniqueIdentifier(), testUpdateDto);

        var response = new InformativeResponse();
        response.code = 200;
        response.message = "Test was successfully updated.";

        return Response.ok(response).build();
    }

    @Tag(name = "Test")
    @Operation(
            summary = "Delete Test",
            description = "Deletes a specific Test item by ID.")
    @APIResponse(
            responseCode = "200",
            description = "Test item deleted.",
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
    public Response deleteTest(
            @Parameter(
                    description = "The ID of the Test to be deleted.",
                    required = true,
                    example = "pid_graph:3E109BBA",
                    schema = @Schema(type = SchemaType.STRING))
            @PathParam("id")
            @Valid @NotFoundEntity(repository = TestRepository.class, message = "There is no Test with the following id:") String id) {

        var deleted = testService.deleteTest(id);

        InformativeResponse informativeResponse = new InformativeResponse();

        if (!deleted) {

            informativeResponse.code = 500;
            informativeResponse.message = "Test hasn't been deleted. An error occurred.";
        } else {

            informativeResponse.code = 200;
            informativeResponse.message = "Test has been successfully deleted.";
        }

        return Response.ok().entity(informativeResponse).build();

    }

    @Tag(name = "Test")
    @Operation(
            summary = "List all Tests",
            description = "Retrieves a paginated list of Tests."
    )
    @APIResponse(
            responseCode = "200",
            description = "List of Tests.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = PageableTestResponse.class)))
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
    public Response listTests(
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

        var tests = testService.getTestAndTestDefinitionListAll(page - 1, size, uriInfo);

        return Response.ok().entity(tests).build();
    }

    public static class PageableTestResponse extends PageResource<TestResponseDto> {

        private List<TestResponseDto> content;

        @Override
        public List<TestResponseDto> getContent() {
            return content;
        }

        @Override
        public void setContent(List<TestResponseDto> content) {
            this.content = content;
        }
    }


}
