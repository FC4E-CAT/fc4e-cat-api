package org.grnet.cat.api.endpoints;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.grnet.cat.dtos.InformativeResponse;
import org.grnet.cat.dtos.statistics.StatisticsResponse;
import org.grnet.cat.services.StatisticsService;

@Path("/v1/statistics")
public class StatisticsEndpoint {

    @Inject
    StatisticsService statisticsService;

    @Tag(name = "Statistics")
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
            responseCode = "500",
            description = "Internal Server Error.",
            content = @Content(schema = @Schema(
                    type = SchemaType.OBJECT,
                    implementation = InformativeResponse.class)))
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStatisticsRequest() {

        var statistics = statisticsService.getStatistics();

        return Response.ok().entity(statistics).build();
    }
}
