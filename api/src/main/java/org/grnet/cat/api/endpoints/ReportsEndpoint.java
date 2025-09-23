package org.grnet.cat.api.endpoints;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeIn;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.grnet.cat.api.filters.Registration;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.report.*;
import org.grnet.cat.repositories.ReportRepository;
import org.grnet.cat.services.report.ReportService;
import org.grnet.cat.utils.Utility;

@Path("/v1/reports")
@Authenticated
@SecurityScheme(securitySchemeName = "Authentication",
        description = "JWT token",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER)
public class ReportsEndpoint {

    @Inject
    ReportService reportService;

    @Inject
    Utility utility;

    @Tag(name = "Reports")
    @Operation(
            summary = "List report definitions",
            description = "Returns all predefined report definitions available for execution."
    )
    @APIResponse(
            responseCode = "200",
            description = "List of available report definitions",
            content = @Content(schema = @Schema(
                    type = SchemaType.ARRAY,
                    implementation = ReportDefinitionDto.class))
    )
    @APIResponse(
            responseCode = "401",
            description = "User has not been authenticated.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class))
    )
    @APIResponse(
            responseCode = "403",
            description = "Not permitted.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class))
    )
    @APIResponse(
            responseCode = "500",
            description = "Internal Server Error.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class))
    )
    @SecurityRequirement(name = "Authentication")
    @GET
    @Path("/definitions")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response getDefinitions() {

        var response = reportService.getAllDefinitions();

        return Response.ok().entity(response).build();
    }

    @Tag(name = "Reports")
    @Operation(
            summary = "Get a report definition",
            description = "Returns a predefined report by Id."
    )
    @APIResponse(
            responseCode = "200",
            description = "Get a report definition",
            content = @Content(schema = @Schema(
                    type = SchemaType.ARRAY,
                    implementation = ReportDefinitionDto.class))
    )
    @APIResponse(
            responseCode = "401",
            description = "User has not been authenticated.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class))
    )
    @APIResponse(
            responseCode = "403",
            description = "Not permitted.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class))
    )
    @APIResponse(
            responseCode = "500",
            description = "Internal Server Error.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class))
    )
    @SecurityRequirement(name = "Authentication")
    @GET
    @Path("/definitions/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response getDefinition(
            @Parameter(
                    description = "The ID of the report definition.",
                    required = true,
                    example = "1",
                    schema = @Schema(type = SchemaType.STRING))
            @PathParam("id") @NotFoundEntity(repository = ReportRepository.class, message = "There is no Report with the following id:") Long id) {

        var response = reportService.getDefinitionById(id);

        return Response.ok().entity(response).build();
    }

    @Tag(name = "Reports")
    @Operation(
            summary = "Get available report filters",
            description = "Returns the list of motivations and publication status values that can be used as filters when running reports."
    )
    @APIResponse(
            responseCode = "200",
            description = "Filters returned successfully",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = ReportFiltersListDto.class))
    )
    @APIResponse(
            responseCode = "401",
            description = "User has not been authenticated.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class))
    )
    @APIResponse(
            responseCode = "403",
            description = "Not permitted.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class))
    )
    @APIResponse(
            responseCode = "500",
            description = "Internal Server Error.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class))
    )
    @SecurityRequirement(name = "Authentication")
    @GET
    @Path("/filters/by-report-definition/{report-id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response getFilters(@Parameter(
            description = "The ID of the report definition.",
            required = true,
            example = "1",
            schema = @Schema(type = SchemaType.STRING))
                                   @PathParam("report-id") @NotFoundEntity(repository = ReportRepository.class, message = "There is no Report with the following id:") Long reportId) {

        var response = reportService.getAllFilters(reportId);

        return Response.ok().entity(response).build();
    }


    /**
     * Runs an ad-hoc report definition and returns the generated table.
     *
     * @param request Report definition (rows, columns, values, filters).
     * @return Report results including rows, columns, and data.
     */

    @Tag(name = "Reports")
    @Operation(
            summary = "Generate an ad-hoc report",
            description = "This endpoint executes a report definition provided in the path and returns the generated results as a table."
    )
    @APIResponse(
            responseCode = "200",
            description = "Report results with rows, columns, and data matrix",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = ReportResponseDto.class))
    )
    @APIResponse(
            responseCode = "401",
            description = "User has not been authenticated.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class))
    )
    @APIResponse(
            responseCode = "403",
            description = "Not permitted.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class))
    )
    @APIResponse(
            responseCode = "500",
            description = "Internal Server Error.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class))
    )
    @SecurityRequirement(name = "Authentication")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Registration

    @Path("/generate/{id}")
    public Response generate(@Parameter(
                                     description = "The ID of the report definition.",
                                     required = true,
                                     example = "1",
                                     schema = @Schema(type = SchemaType.STRING))
                             @PathParam("id") @NotFoundEntity(repository = ReportRepository.class, message = "There is no Report with the following id:") Long id,
                             @RequestBody(
                                     description = "Filters to apply when generating the report.",
                                     required = true,
                                     content = @Content(
                                             schema = @Schema(implementation = ReportFilterDto.class),
                                             examples = {
                                                     @ExampleObject(
                                                             name = "Actor x Assessments",
                                                             summary = "Example for report ID = 1",
                                                             value = "{\n" +
                                                                     "  \"filters\": {\n" +
                                                                     "    \"motivations\": [\n" +
                                                                     "      \"pid_graph:3E109BBA\"\n" +
                                                                     "    ],\n" +
                                                                     "    \"publication_status\": [\n" +
                                                                     "      \"published\",\n" +
                                                                     "      \"unpublished\"\n" +
                                                                     "    ]\n" +
                                                                     "  }\n" +
                                                                     "}"
                                                     ),
                                                     @ExampleObject(
                                                             name = "Organisations × Assessments",
                                                             summary = "Example for report ID = 2",
                                                             value = "{\n" +
                                                                     "  \"filters\": {\n" +
                                                                     "    \"motivations\": [\n" +
                                                                     "      \"pid_graph:3E109BBA\"\n" +
                                                                     "    ],\n" +
                                                                     "    \"publication_status\": [\n" +
                                                                     "      \"published\",\n" +
                                                                     "      \"unpublished\"\n" +
                                                                     "    ]\n" +
                                                                     "  }\n" +
                                                                     "}"
                                                     )


                                             }
                                     )
                             )
                             @Valid
                             @NotNull(message = "The request body is empty.")
                             ReportFilterDto request) {

        var response = reportService.run(id, request, utility.getUserUniqueIdentifier());
        return Response.ok().entity(response).build();
    }

}
