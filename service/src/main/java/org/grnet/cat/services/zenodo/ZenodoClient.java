package org.grnet.cat.services.zenodo;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;
import java.util.Map;


@RegisterRestClient
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ZenodoClient {

    @POST
    @Path("/api/deposit/depositions")
    Map<String, Object> createDeposit(@HeaderParam("Authorization") String token, Map<String, Object> emptyBody) throws WebApplicationException, ProcessingException;

    @PUT
    @Path("/api/deposit/depositions/{id}")
    Map<String, Object> updateMetadata(@HeaderParam("Authorization") String token, @PathParam("id") String id, Map<String, Object> metadata) throws WebApplicationException, ProcessingException;

    @POST
    @Path("/api/deposit/depositions/{id}/actions/publish")
    Map<String, Object> publishDeposit(@HeaderParam("Authorization") String token, @PathParam("id") String id) throws WebApplicationException, ProcessingException;

    @GET
    @Path("/api/deposit/depositions")
    List<Map<String, Object>> listDeposits(@HeaderParam("Authorization") String token);

    @DELETE
    @Path("/api/deposit/depositions/{id}")
    Map<String, Object> deleteDeposit(@HeaderParam("Authorization") String token, @PathParam("id") String id) throws WebApplicationException, ProcessingException;

    @POST
    @Path("/api/deposit/depositions/{id}/files")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    Map<String, Object> uploadFile(@HeaderParam("Authorization") String token, @PathParam("id") String depositionId, byte[] fileBytes, @HeaderParam("Content-Type") String contentType) throws WebApplicationException, ProcessingException;
    @GET
    @Path("/api/deposit/depositions/{id}")
    Map<String, Object> getDeposit(@HeaderParam("Authorization") String token, @PathParam("id") String id) throws WebApplicationException, ProcessingException;

}