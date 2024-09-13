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
import org.grnet.cat.repositories.registry.TestMethodRepository;
import org.grnet.cat.services.registry.TestMethodService;
import org.grnet.cat.utils.Utility;

import java.util.List;

import static org.eclipse.microprofile.openapi.annotations.enums.ParameterIn.QUERY;

@Path("/v1/registry/tests/test-method")
@Authenticated
public class TestMethodEndpoint {
    @Inject
    Utility utility;

    @Inject
    TestMethodService testMethodService;
    @ConfigProperty(name = "api.server.url")
    String serverUrl;
    @Tag(name = "Test")
    @Operation(
            summary = "Get Test Method by ID",
            description = "Retrieves a specific Test Method item by ID."
    )
    @APIResponse(
            responseCode = "200",
            description = "The corresponding Test Method item.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = TestMethodResponseDto.class))
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
    public Response getTestMethodById(
            @Parameter(
                    description = "The ID of the Test Method to retrieve.",
                    required = true,
                    example = "pid_graph:03615660",
                    schema = @Schema(type = SchemaType.STRING))
            @PathParam("id")
            @Valid @NotFoundEntity(repository = TestMethodRepository.class, message = "There is no Test Method with the following id:") String id)
    {
        var response = testMethodService.getTestMethodById(id);

        return Response.ok(response).build();
    }

    @Tag(name = "Test")
    @Operation(
            summary     = "Create new Test Method",
            description = "Creates a new Test Method item."
    )
    @APIResponse(
            responseCode = "201",
            description = "Test Method item created.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = TestMethodResponseDto.class))
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
    public Response createTestMethod(
            @Valid @NotNull(message = "The request body is empty.") TestMethodRequestDto testRequestDto, @Context UriInfo uriInfo) {

        var testMethod = testMethodService.createTestMethod(utility.getUserUniqueIdentifier(),testRequestDto);
        var serverInfo = new CatServiceUriInfo(serverUrl.concat(uriInfo.getPath()));

        return Response.created(serverInfo.getAbsolutePathBuilder().path(String.valueOf(testMethod.id)).build()).entity(testMethod).build();
    }

    @Tag(name = "Test")
    @Operation(
            summary = "Update TestMethod",
            description = "Updates an existing TestMethod item."
    )
    @APIResponse(
            responseCode = "200",
            description = "Subject was updated successfully.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = TestMethodResponseDto.class)))
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
    public Response updateTestMethod(
            @Parameter(
                    description = "The ID of the TestMethod to update.",
                    required = true,
                    example = "pid_graph:9F1A6267",
                    schema = @Schema(type = SchemaType.STRING))
            @PathParam("id")
            @Valid @NotFoundEntity(repository = TestMethodRepository.class, message = "There is no Test Method with the following id:") String id,
            @Valid TestMethodUpdateDto testMethodUpdateDto) {

        var updatedDto = testMethodService.updateTestMethod(id, utility.getUserUniqueIdentifier(), testMethodUpdateDto);

        return Response.ok(updatedDto).build();
    }

    @Tag(name = "Test")
    @Operation(
            summary = "Delete Test Method",
            description = "Deletes a specific Test Method item by ID.")
    @APIResponse(
            responseCode = "200",
            description = "Test Method item deleted.",
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
    public Response deleteTestMethod(
            @Parameter(
                    description = "The ID of the Test Method to be deleted.",
                    required = true,
                    example = "pid_graph:3E109BBA",
                    schema = @Schema(type = SchemaType.STRING))
            @PathParam("id")
            @Valid @NotFoundEntity(repository = TestMethodRepository.class, message = "There is no Test Method with the following id:") String id) {

        var deleted =  testMethodService.deleteTestMethod(id);

        InformativeResponse informativeResponse = new InformativeResponse();

        if (!deleted) {

            informativeResponse.code =500;
            informativeResponse.message = "TestMethod hasn't been deleted. An error occurred.";
        } else {

            informativeResponse.code = 200;
            informativeResponse.message = "TestMethod has been successfully deleted.";
        }

        return Response.ok().entity(informativeResponse).build();

    }

    @Tag(name = "Test")
    @Operation(
            summary = "List all Test Methods",
            description = "Retrieves a paginated list of Test Methods."
    )
    @APIResponse(
            responseCode = "200",
            description = "List of Test Methods.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = PageableTestMethodResponse.class)))
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
    public Response listTestMethods(
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

        var testMethods = testMethodService.getTestMethodlistAll(page - 1, size, uriInfo);

        return Response.ok().entity(testMethods).build();
    }

    public static class PageableTestMethodResponse extends PageResource<TestMethodResponseDto> {

        private List<TestMethodResponseDto> content;

        @Override
        public List<TestMethodResponseDto> getContent() {
            return content;
        }

        @Override
        public void setContent(List<TestMethodResponseDto> content) {
            this.content = content;
        }
    }
}
