package org.grnet.cat.services.clients;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@RegisterRestClient
@Path("/")
public interface HttpsTestClient {

    @GET
    @Path("/status")
    @Produces(MediaType.APPLICATION_JSON)
    String checkStatus();
}
