package org.grnet.cat.api.endpoints.registry;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.grnet.cat.dtos.InformativeResponse;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeIn;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.grnet.cat.api.filters.Registration;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.RelationResponse;
import org.grnet.cat.dtos.registry.codelist.*;
import org.grnet.cat.repositories.registry.*;
import org.grnet.cat.services.registry.*;

import java.util.List;

import static org.eclipse.microprofile.openapi.annotations.enums.ParameterIn.QUERY;

@Path("/v1/registry")
@Authenticated
@SecurityScheme(securitySchemeName = "Authentication",
        description = "JWT token",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER)
public class RegistryCodelistEndpoint {

    @Inject
    TypeCriterionService typeCriterionService;
    @Inject
    ImperativeService imperativeService;
    @Inject
    TypeBenchmarkService typeBenchmarkService;

    @Inject
    RegistryActorService registryActorService;

    @Inject
    RelationService relationService;

    @Inject
    MotivationTypeService motivationTypeService;
    @Tag(name = "Registry Codelist")
    @Operation(
            summary = "Get specific Type Criterion.",
            description = "Returns a specific Type Criterion.")
    @APIResponse(
            responseCode = "200",
            description = "The corresponding Type Criterion.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = TypeCriterionResponse.class)))
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
    @Path("/criterion-types/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response getTypeCriterion(@Parameter(
            description = "The ID of the Type Criterion to retrieve.",
            required = true,
            example = "pid_graph:3E109BBA",
            schema = @Schema(type = SchemaType.STRING))
                                     @PathParam("id")
                                     @Valid @NotFoundEntity(repository = TypeCriterionRepository.class, message = "There is no Type Criterion with the following id:") String id) {

        var typeCriterion = typeCriterionService.getTypeCriterionById(id);
        return Response.ok().entity(typeCriterion).build();
    }

    @Tag(name = "Registry Codelist")
    @Operation(
            summary = "Get list of Type Criterion.",
            description = "This endpoint retrieves all Type Criterion." +
                    "By default, the first page of 10 Type Criterion will be returned. You can tune the default values by using the query parameters page and size.")
    @APIResponse(
            responseCode = "200",
            description = "List of Type Criterion.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = PageableTypeCriterionResponse.class)))
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
    @Path("/criterion-types")
    public Response getTypeCriterionList(@Parameter(name = "page", in = QUERY,
            description = "Indicates the page number. Page number must be >= 1.") @DefaultValue("1") @Min(value = 1, message = "Page number must be >= 1.") @QueryParam("page") int page,
                                         @Parameter(name = "size", in = QUERY,
                                                 description = "The page size.") @DefaultValue("10") @Min(value = 1, message = "Page size must be between 1 and 100.")
                                         @Max(value = 100, message = "Page size must be between 1 and 100.") @QueryParam("size") int size,
                                         @Context UriInfo uriInfo) {

        var typeCriterionList = typeCriterionService.getTypeCriterionListByPage(page - 1, size, uriInfo);

        return Response.ok().entity(typeCriterionList).build();
    }

    public static class PageableTypeCriterionResponse extends PageResource<TypeCriterionResponse> {

        private List<TypeCriterionResponse> content;

        @Override
        public List<TypeCriterionResponse> getContent() {
            return content;
        }

        @Override
        public void setContent(List<TypeCriterionResponse> content) {
            this.content = content;
        }
    }

    @Tag(name = "Registry Codelist")
    @Operation(
            summary = "Get specific Imperative.",
            description = "Returns a specific Imperative.")
    @APIResponse(
            responseCode = "200",
            description = "The corresponding Imperative.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = ImperativeResponse.class)))
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
    @Path("/imperatives/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response getImpertive(@Parameter(
            description = "The ID of the Imperative to retrieve.",
            required = true,
            example = "pid_graph:3E109BBA",
            schema = @Schema(type = SchemaType.STRING))
                                 @PathParam("id")
                                 @Valid @NotFoundEntity(repository = ImperativeRepository.class, message = "There is no Imperative with the following id:") String id) {

        var imperative = imperativeService.getImperativeById(id);
        return Response.ok().entity(imperative).build();
    }

    @Tag(name = "Registry Codelist")
    @Operation(
            summary = "Get list of Imperative.",
            description = "This endpoint retrieves all Imperative." +
                    "By default, the first page of 10 Imperative will be returned. You can tune the default values by using the query parameters page and size.")
    @APIResponse(
            responseCode = "200",
            description = "List of Imperative.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = PageableImperativeResponse.class)))
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
    @Path("/imperatives")
    public Response getImperativeList(@Parameter(name = "page", in = QUERY,
            description = "Indicates the page number. Page number must be >= 1.") @DefaultValue("1") @Min(value = 1, message = "Page number must be >= 1.") @QueryParam("page") int page,
                                      @Parameter(name = "size", in = QUERY,
                                              description = "The page size.") @DefaultValue("10") @Min(value = 1, message = "Page size must be between 1 and 100.")
                                      @Max(value = 100, message = "Page size must be between 1 and 100.") @QueryParam("size") int size,
                                      @Context UriInfo uriInfo) {

        var imperativeList = imperativeService.getImperativeListByPage(page - 1, size, uriInfo);

        return Response.ok().entity(imperativeList).build();
    }

    public static class PageableImperativeResponse extends PageResource<ImperativeResponse> {

        private List<ImperativeResponse> content;

        @Override
        public List<ImperativeResponse> getContent() {
            return content;
        }

        @Override
        public void setContent(List<ImperativeResponse> content) {
            this.content = content;
        }
    }


    @Tag(name = "Registry Codelist")
    @Operation(
            summary = "Get specific TypeBenchmark.",
            description = "Returns a specific TypeBenchmark.")
    @APIResponse(
            responseCode = "200",
            description = "The corresponding TypeBenchmark.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = TypeBenchmarkResponse.class)))
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
    @Path("/benchmark-types/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response getTypeBenchmark(@Parameter(
            description = "The ID of the Type Benchmark to retrieve.",
            required = true,
            example = "pid_graph:3E109BBA",
            schema = @Schema(type = SchemaType.STRING))
                                     @PathParam("id")
                                     @Valid @NotFoundEntity(repository = TypeBenchmarkRepository.class, message = "There is no TypeBenchmark with the following id:") String
                                             id) {

        var typeBenchmark = typeBenchmarkService.getTypeBenchmarkById(id);
        return Response.ok().entity(typeBenchmark).build();
    }

    @Tag(name = "Registry Codelist")
    @Operation(
            summary = "Get list of TypeBenchmark.",
            description = "This endpoint retrieves all TypeBenchmarks." +
                    "By default, the first page of 10 TypeBenchmark will be returned. You can tune the default values by using the query parameters page and size.")
    @APIResponse(
            responseCode = "200",
            description = "List of TypeBenchmark.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = PageableTypeBenchmarkResponse.class)))
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
    @Path("/benchmark-types")
    public Response getTypeBenchmarkList(@Parameter(name = "page", in = QUERY,
            description = "Indicates the page number. Page number must be >= 1.") @DefaultValue("1") @Min(value = 1, message = "Page number must be >= 1.") @QueryParam("page") int page,
                                         @Parameter(name = "size", in = QUERY,
                                                 description = "The page size.") @DefaultValue("10") @Min(value = 1, message = "Page size must be between 1 and 100.")
                                         @Max(value = 100, message = "Page size must be between 1 and 100.") @QueryParam("size") int size,
                                         @Context UriInfo uriInfo) {

        var typeBenchmarkList = typeBenchmarkService.getTypeBenchmarkListByPage(page - 1, size, uriInfo);

        return Response.ok().entity(typeBenchmarkList).build();
    }

    public static class PageableTypeBenchmarkResponse extends PageResource<TypeBenchmarkResponse> {

        private List<TypeBenchmarkResponse> content;

        @Override
        public List<TypeBenchmarkResponse> getContent() {
            return content;
        }

        @Override
        public void setContent(List<TypeBenchmarkResponse> content) {
            this.content = content;
        }
    }

    @Tag(name = "Registry Codelist")
    @Operation(
            summary = "Get specific Actor.",
            description = "Returns a specific Actor.")
    @APIResponse(
            responseCode = "200",
            description = "The corresponding Actor.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = RegistryActorResponse.class)))
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
    @Path("/actors/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response getActor(@Parameter(
            description = "The ID of the Actor to retrieve.",
            required = true,
            example = "pid_graph:3E109BBA",
            schema = @Schema(type = SchemaType.STRING))
                                     @PathParam("id")
                                     @Valid @NotFoundEntity(repository = RegistryActorRepository.class, message = "There is no Type Criterion with the following id:") String id) {

        var actor = registryActorService.getActorById(id);
        return Response.ok().entity(actor).build();
    }

    @Tag(name = "Registry Codelist")
    @Operation(
            summary = "Get list of Actor.",
            description = "This endpoint retrieves all Actors" +
                    "By default, the first page of 10 Actor will be returned. You can tune the default values by using the query parameters page and size.")
    @APIResponse(
            responseCode = "200",
            description = "List of Type Criterion.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = PageableRegistryActorResponse.class)))
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
    @Path("/actors")
    public Response getActorList(@Parameter(name = "page", in = QUERY,
            description = "Indicates the page number. Page number must be >= 1.") @DefaultValue("1") @Min(value = 1, message = "Page number must be >= 1.") @QueryParam("page") int page,
                                         @Parameter(name = "size", in = QUERY,
                                                 description = "The page size.") @DefaultValue("10") @Min(value = 1, message = "Page size must be between 1 and 100.")
                                         @Max(value = 100, message = "Page size must be between 1 and 100.") @QueryParam("size") int size,
                                         @Context UriInfo uriInfo) {

        var actorList = registryActorService.getActorListByPage(page - 1, size, uriInfo);

        return Response.ok().entity(actorList).build();
    }

    public static class PageableRegistryActorResponse extends PageResource<RegistryActorResponse> {

        private List<RegistryActorResponse> content;

        @Override
        public List<RegistryActorResponse> getContent() {
            return content;
        }

        @Override
        public void setContent(List<RegistryActorResponse> content) {
            this.content = content;
        }
    }



    @Tag(name = "Registry Codelist")
    @Operation(
            summary = "Get specific MotivationType.",
            description = "Returns a specific MotivationType.")
    @APIResponse(
            responseCode = "200",
            description = "The corresponding MotivationType.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = MotivationTypeResponse.class)))
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
    @Path("/motivation-types/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response getMotivationType(@Parameter(
            description = "The ID of the Motivation Type to retrieve.",
            required = true,
            example = "pid_graph:3E109BBA",
            schema = @Schema(type = SchemaType.STRING))
                                     @PathParam("id")
                                     @Valid @NotFoundEntity(repository = MotivationTypeRepository.class, message = "There is no MotivationType with the following id:") String
                                             id) {

        var motivationType = motivationTypeService.getMotivationTypeById(id);
        return Response.ok().entity(motivationType).build();
    }

    @Tag(name = "Registry Codelist")
    @Operation(
            summary = "Get list of MotivationType.",
            description = "This endpoint retrieves all MotivationTypes." +
                    "By default, the first page of 10 MotivationType will be returned. You can tune the default values by using the query parameters page and size.")
    @APIResponse(
            responseCode = "200",
            description = "List of TypeBenchmark.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = PageableMotivationTypeResponse.class)))
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
    @Path("/motivation-types")
    public Response getMotivationTypeList(@Parameter(name = "page", in = QUERY,
            description = "Indicates the page number. Page number must be >= 1.") @DefaultValue("1") @Min(value = 1, message = "Page number must be >= 1.") @QueryParam("page") int page,
                                         @Parameter(name = "size", in = QUERY,
                                                 description = "The page size.") @DefaultValue("10") @Min(value = 1, message = "Page size must be between 1 and 100.")
                                         @Max(value = 100, message = "Page size must be between 1 and 100.") @QueryParam("size") int size,
                                         @Context UriInfo uriInfo) {

        var motivationTypeList = motivationTypeService.getMotivationTypeListByPage(page - 1, size, uriInfo);

        return Response.ok().entity(motivationTypeList).build();
    }

    public static class PageableMotivationTypeResponse extends PageResource<MotivationTypeResponse> {

        private List<MotivationTypeResponse> content;

        @Override
        public List<MotivationTypeResponse> getContent() {
            return content;
        }

        @Override
        public void setContent(List<MotivationTypeResponse> content) {
            this.content = content;
        }
    }


    @Tag(name = "Registry Codelist")
    @Operation(
            summary = "Get specific relation.",
            description = "Returns a specific Relation.")
    @APIResponse(
            responseCode = "200",
            description = "The corresponding Relation.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = RelationResponse.class)))
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
    @Path("/relations/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Registration
    public Response getRelation(@Parameter(
            description = "The ID of the Relation to retrieve.",
            required = true,
            example = "pid_graph:3E109BBA",
            schema = @Schema(type = SchemaType.STRING))
                                      @PathParam("id")
                                      @Valid @NotFoundEntity(repository = RelationRepository.class, message = "There is no Relation with the following id:") String
                                              id) {

        var relation = relationService.getRelationById(id);
        return Response.ok().entity(relation).build();
    }

    @Tag(name = "Registry Codelist")
    @Operation(
            summary = "Get list of Relation.",
            description = "This endpoint retrieves all Relation." +
                    "By default, the first page of 10 Relation will be returned. You can tune the default values by using the query parameters page and size.")
    @APIResponse(
            responseCode = "200",
            description = "List of TypeBenchmark.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = PageableRelationResponse.class)))
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
    @Path("/relations")
    public Response getRelationList(@Parameter(name = "page", in = QUERY,
            description = "Indicates the page number. Page number must be >= 1.") @DefaultValue("1") @Min(value = 1, message = "Page number must be >= 1.") @QueryParam("page") int page,
                                          @Parameter(name = "size", in = QUERY,
                                                  description = "The page size.") @DefaultValue("10") @Min(value = 1, message = "Page size must be between 1 and 100.")
                                          @Max(value = 100, message = "Page size must be between 1 and 100.") @QueryParam("size") int size,
                                          @Context UriInfo uriInfo) {

        var relationList =relationService.getRelationListByPage(page - 1, size, uriInfo);

        return Response.ok().entity(relationList).build();
    }

    public static class PageableRelationResponse extends PageResource<RelationResponse> {

        private List<RelationResponse> content;

        @Override
        public List<RelationResponse> getContent() {
            return content;
        }

        @Override
        public void setContent(List<RelationResponse> content) {
            this.content = content;
        }
    }



}
