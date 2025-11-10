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
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.grnet.cat.api.filters.Registration;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.dtos.CsvResponseDto;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.assessment.registry.UserJsonRegistryAssessmentResponse;
import org.grnet.cat.dtos.report.*;
import org.grnet.cat.repositories.MotivationAssessmentRepository;
import org.grnet.cat.repositories.ReportRepository;
import org.grnet.cat.services.assessment.JsonAssessmentService;
import org.grnet.cat.services.report.ReportService;
import org.grnet.cat.utils.Utility;

import static org.eclipse.microprofile.openapi.annotations.enums.ParameterIn.QUERY;

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


    /**
     * Injection point for the JsonAssessment service
     */
    @Inject
    JsonAssessmentService assessmentService;

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
    public Response generate(
            @Parameter(
            description = "The ID of the report definition.",
            required = true,
            example = "1",
            schema = @Schema(type = SchemaType.STRING))
            @PathParam("id")
            @NotFoundEntity(repository = ReportRepository.class, message = "There is no Report with the following id:") Long id,
            @RequestBody(
                    description = "Filters to apply when generating the report.",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ReportFilterDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Actors × Assessments",
                                            summary = "Example for report ID = 1",
                                            value = "{\n" +
                                                    "  \"filters\": {\n" +
                                                    "    \"actor\": [\n" +
                                                    "      \"pid_graph:0E00C332\"\n" +
                                                    "    ],\n" +
                                                    "    \"motivation\": [\n" +
                                                    "      \"pid_graph:3E109BBA\"\n" +
                                                    "    ],\n" +
                                                    "    \"publication_status\": [\n" +
                                                    "      \"1\",\n" +
                                                    "      \"2\"\n" +
                                                    "    ]\n" +
                                                    "  }\n" +
                                                    "}"
                                    ),
                                    @ExampleObject(
                                            name = "Organisations × Assessments",
                                            summary = "Example for report ID = 2",
                                            value = "{\n" +
                                                    "  \"filters\": {\n" +
                                                    "    \"organisation\": [\n" +
                                                    "      \"05tcasm11\"\n" +
                                                    "    ],\n" +
                                                    "    \"motivation\": [\n" +
                                                    "      \"pid_graph:3E109BBA\"\n" +
                                                    "    ],\n" +
                                                    "    \"publication_status\": [\n" +
                                                    "      \"1\",\n" +
                                                    "      \"2\"\n" +
                                                    "    ]\n" +
                                                    "  }\n" +
                                                    "}"
                                    ),
                                    @ExampleObject(
                                            name = "Subject × Motivation/Actor",
                                            summary = "Example for report ID = 3",
                                            value = "{\n" +
                                                    "  \"filters\": {\n" +
                                                    "    \"subject\": [\n" +
                                                    "      \"1000\"\n" +
                                                    "    ],\n" +
                                                    "    \"actor\": [\n" +
                                                    "      \"pid_graph:0E00C332\"\n" +
                                                    "    ],\n" +
                                                    "    \"motivation\": [\n" +
                                                    "      \"pid_graph:3E109BBA\"\n" +
                                                    "    ],\n" +
                                                    "    \"publication_status\": [\n" +
                                                    "      \"1\",\n" +
                                                    "      \"2\"\n" +
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

    @Tag(name = "Reports")
    @Operation(
            summary = "Export a report",
            description = "Converts a generated report (JSON) into CSV for download."
    )
    @APIResponse(
            responseCode = "200",
            description = "CSV export of the report results",
            content = @Content(
                    schema = @Schema(
                            type = SchemaType.OBJECT,
                            implementation = CsvResponseDto.class)
            )
    )
    @APIResponse(
            responseCode = "401",
            description = "User has not been authenticated.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class))
    )
    @APIResponse(
            responseCode = "403",
            description = "Not permitted.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(
                            type = SchemaType.OBJECT,
                            implementation = InformativeResponse.class))    )
    @APIResponse(
            responseCode = "500",
            description = "Internal Server Error.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = InformativeResponse.class))
    )
    @SecurityRequirement(name = "Authentication")
    @POST
    @Path("/export/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, "text/csv"})
    @Registration
    public Response exportReport(
            @Parameter(
                    description = "The ID of the report definition.",
                    required = true,
                    example = "1",
                    schema = @Schema(type = SchemaType.STRING))
            @PathParam("id") Long id,
            @RequestBody(
                    description = "The generated report response to convert into CSV.",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ReportResponseDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Actors × Assessments",
                                            summary = "Export report ID = 1 (Actors)",
                                            value = "{\n" +
                                                    "  \"label\": \"Actors × Assessments\",\n" +
                                                    "  \"description\": \"Assessment results per actor\",\n" +
                                                    "  \"rows_dimension\": \"actor\",\n" +
                                                    "  \"columns_dimension\": \"assessment\",\n" +
                                                    "  \"value_type\": \"compliance\",\n" +
                                                    "  \"created_by\": \"admin_voperson_id\",\n" +
                                                    "  \"created_on\": \"2025-09-29T11:05:59.384882Z\",\n" +
                                                    "  \"rows\": [\"PID Manager (Role)\", \"PID Owner (Role)\"],\n" +
                                                    "  \"columns\": [\"assessment-1\", \"assessment-2\", \"assessment-3\"],\n" +
                                                    "  \"data\": [\n" +
                                                    "    [\"FAIL\", \"N/A\", \"N/A\"],\n" +
                                                    "    [\"N/A\", \"PASS\", \"PASS\"]\n" +
                                                    "  ]\n" +
                                                    "}"
                                    ),
                                    @ExampleObject(
                                            name = "Organisations × Assessments",
                                            summary = "Export report ID = 2 (Organisations)",
                                            value = "{\n" +
                                                    "  \"label\": \"Organisations × Assessments\",\n" +
                                                    "  \"description\": \"Assessment results per organisation\",\n" +
                                                    "  \"rows_dimension\": \"organisation\",\n" +
                                                    "  \"columns_dimension\": \"assessment\",\n" +
                                                    "  \"value_type\": \"compliance\",\n" +
                                                    "  \"created_by\": \"admin_voperson_id\",\n" +
                                                    "  \"created_on\": \"2025-09-29T11:06:12.111Z\",\n" +
                                                    "  \"rows\": [\"GRNET S.A\"],\n" +
                                                    "  \"columns\": [\"assessment-2\", \"assessment-3\"],\n" +
                                                    "  \"data\": [\n" +
                                                    "    [\"PASS\", \"FAIL\"]\n" +
                                                    "  ]\n" +
                                                    "}"
                                    ),
                                    @ExampleObject(
                                            name = "Subjects × Motivation/Actor",
                                            summary = "Export report ID = 3 (Subjects × Motivation/Actor)",
                                            value = "{\n" +
                                                    "  \"label\": \"Subjects × Motivation/Actor\",\n" +
                                                    "  \"description\": \"Shows the latest assessment compliance for each subject per motivation/actor\",\n" +
                                                    "  \"rows_dimension\": \"subject\",\n" +
                                                    "  \"columns_dimension\": \"motivation_actor\",\n" +
                                                    "  \"value_type\": \"compliance\",\n" +
                                                    "  \"created_by\": \"admin_voperson_id\",\n" +
                                                    "  \"created_on\": \"2025-10-02T13:10:00.000Z\",\n" +
                                                    "  \"rows\": [\"test-name\"],\n" +
                                                    "  \"columns\": [\"EOSC PID Policy / PID Owner (Role)\", \"EOSC PID Policy / PID Manager (Role)\"],\n" +
                                                    "  \"data\": [\n" +
                                                    "    [\"PASS\", \"FAIL\"]\n" +
                                                    "  ],\n" +
                                                    "}"
                                    )
                            }
                    )
            )
            @Valid @NotNull(message = "The request body is empty.")
            ReportResponseDto report) {

        var csvContent = reportService.exportToCsv(report);

        var safeLabel = report.label != null
                ? report.label.replaceAll("[^a-zA-Z0-9 ]", "")
                .trim()
                .replaceAll("\\s+", "_")
                : "Report_" + id;

        var filename = "CAT_Report_" + safeLabel + ".csv";

        return Response.ok(csvContent)
                .header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
                .type("text/csv")
                .build();

    }
    @Tag(name = "Reports")
    @Operation(
            summary = "Retrieve all assessments.",
            description = "Retrieves a list of all assessments submitted by users." +
                    "By default, the first page of 10 assessments will be returned ordered by the date created. You can tune the default values by using the query parameters page and size.")
    @APIResponse(
            responseCode = "200",
            description = "Successful response with the list of assessments.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = AdminEndpoint.PageablePartialAssessmentResponse.class)))
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

    @Tag(name = "Reports")
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
}