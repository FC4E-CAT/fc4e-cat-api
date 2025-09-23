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

import java.util.Map;

@Path("/naco")
@RegisterRestClient(configKey = "naco-service")
public interface NacoClient {

    @GET
    @Path("/get_entry/{aai-provider-id}")
    @Produces(MediaType.APPLICATION_JSON)
    NacoEntryResponse getEntry(@PathParam("aai-provider-id") String aaiProviderId, @QueryParam("apikey") String apiKey);

    @GET
    @Path("/list_entries")
    @Produces(MediaType.APPLICATION_JSON)
    //Map<String, String> getEntries(@QueryParam("apikey") String apiKey);
    Map<String, NacoEntry> getEntries(@QueryParam("apikey") String apiKey);

    @ClientExceptionMapper
    static WebApplicationException toException(Response response) {

        if (response.getStatus() == 404) {

            return new WebApplicationException("There is no AAI Provider with the provided ID.", 404);
        } else if (response.getStatus() == 403) {

            return new WebApplicationException("Not permitted.", 403);
        } else {

            return new WebApplicationException("The NACO service responded with HTTP " + response.getStatus(), response.getStatus());
        }
    }

    public class NacoEntry {
        public String formatted_name;
        public String iss;

        public String getFormatted_name() {
            return formatted_name;
        }

        public void setFormatted_name(String formatted_name) {
            this.formatted_name = formatted_name;
        }

        public String getIss() {
            return iss;
        }

        public void setIss(String iss) {
            this.iss = iss;
        }
    }


}
