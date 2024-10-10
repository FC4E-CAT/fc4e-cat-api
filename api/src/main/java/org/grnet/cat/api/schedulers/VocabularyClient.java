package org.grnet.cat.api.schedulers;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient
@Path("/terminology-api/api/v1/frontend") // Base path for the endpoint
public interface VocabularyClient {

    @POST
    @Path("/searchConcept") // Path for the specific method
    @Consumes(MediaType.APPLICATION_JSON) // Consuming JSON
    Response getAll(VocabularyScheduler.TerminologyRequest request); // Method accepting a request object


}
