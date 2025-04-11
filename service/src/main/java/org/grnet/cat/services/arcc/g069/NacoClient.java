package org.grnet.cat.services.arcc.g069;

import io.quarkus.rest.client.reactive.ClientExceptionMapper;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/naco")
@RegisterRestClient(configKey = "naco-service")
public interface NacoClient {

    @GET
    @Path("/get_entry/{aai-provider-id}")
    @Produces(MediaType.APPLICATION_JSON)
    NacoEntryResponse getEntry(@PathParam("aai-provider-id") String aaiProviderId, @QueryParam("apikey") String apiKey);

    @ClientExceptionMapper
    static WebApplicationException toException(Response response) {

        if (response.getStatus() == 404) {

            return new WebApplicationException("There is no AAI Provider with the provided ID.", 404);
        } else if (response.getStatus() == 403){

            return new WebApplicationException("Not permitted.", 403);
        } else {

            return new WebApplicationException("The NACO service responded with HTTP "+response.getStatus(), response.getStatus());
        }
    }
}
