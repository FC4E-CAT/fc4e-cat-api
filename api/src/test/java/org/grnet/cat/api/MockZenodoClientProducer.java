package org.grnet.cat.api;

import io.quarkus.test.Mock;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.mockito.Mockito;
import org.grnet.cat.services.zenodo.ZenodoClient;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Mock
@ApplicationScoped
public class MockZenodoClientProducer {

    @Produces
    @RestClient
    public ZenodoClient produceMockZenodoClient() {
        // Create a mock instance
        ZenodoClient mockZenodoClient = Mockito.mock(ZenodoClient.class);

        // Mocking methods
        Mockito.when(mockZenodoClient.createDeposit(Mockito.anyString(), Mockito.anyMap()))
                .thenReturn(mockResponse());
        Mockito.when(mockZenodoClient.publishDeposit(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(mockPublishResponse());
        Mockito.when(mockZenodoClient.uploadFile(Mockito.anyString(), Mockito.anyString(), Mockito.any(byte[].class), Mockito.anyString()))
                .thenReturn(mockUploadFileResponse());

        return mockZenodoClient;
    }


    private Map<String, Object> mockResponse() {
        Map<String, Object> depositResponse = new HashMap<>();

        // Add the information to the map
        depositResponse.put("created", "2025-03-20T09:10:17.786607+00:00");
        depositResponse.put("modified", "2025-03-20T09:10:17.860997+00:00");
        depositResponse.put("id", 185547);
        depositResponse.put("conceptrecid", 185546);

        // Metadata
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("title", "test/National Infrastructures for Research and Technology -  GRNET S.A/PID Owner (Role)");
        metadata.put("publication_date", "2025-03-20");
        metadata.put("description", "Publishing assessment test to Zenodo");
        metadata.put("access_right", "open");
        metadata.put("creators", new Object[]{new HashMap<String, Object>() {{
            put("name", "admin admin");
            put("affiliation", null);
        }}});
        metadata.put("license", "cc-zero");
        metadata.put("imprint_publisher", "Zenodo");
        metadata.put("upload_type", "dataset");

        Map<String, Object> prereserveDoi = new HashMap<>();
        prereserveDoi.put("doi", "10.5281/zenodo.185547");
        prereserveDoi.put("recid", 185547);
        metadata.put("prereserve_doi", prereserveDoi);

        depositResponse.put("metadata", metadata);

        // Title
        depositResponse.put("title", "test/National Infrastructures for Research and Technology -  GRNET S.A/PID Owner (Role)");

        // Links
        Map<String, String> links = new HashMap<>();
        links.put("self", "https://sandbox.zenodo.org/api/deposit/depositions/185547");
        links.put("html", "https://sandbox.zenodo.org/deposit/185547");
        links.put("badge", "https://sandbox.zenodo.org/badge/doi/.svg");
        links.put("files", "https://sandbox.zenodo.org/api/deposit/depositions/185547/files");
        links.put("bucket", "https://sandbox.zenodo.org/api/files/c4d0cf4e-a7b2-43d9-8e8d-874d895558e8");
        links.put("latest_draft", "https://sandbox.zenodo.org/api/deposit/depositions/185547");
        links.put("latest_draft_html", "https://sandbox.zenodo.org/deposit/185547");
        links.put("publish", "https://sandbox.zenodo.org/api/deposit/depositions/185547/actions/publish");
        links.put("edit", "https://sandbox.zenodo.org/api/deposit/depositions/185547/actions/edit");
        links.put("discard", "https://sandbox.zenodo.org/api/deposit/depositions/185547/actions/discard");
        links.put("newversion", "https://sandbox.zenodo.org/api/deposit/depositions/185547/actions/newversion");
        links.put("registerconceptdoi", "https://sandbox.zenodo.org/api/deposit/depositions/185547/actions/registerconceptdoi");

        depositResponse.put("links", links);

        // Record ID
        depositResponse.put("record_id", 185547);

        // Owner
        depositResponse.put("owner", 37942);

        // Files (empty in this case)
        depositResponse.put("files", new Object[]{});

        // State
        depositResponse.put("state", "unsubmitted");

        // Submitted status
        depositResponse.put("submitted", false);


        return depositResponse;
    }

    private Map<String, Object> mockUploadFileResponse() {
        Map<String, Object> fileData = new HashMap<>();

        // Add file information
        fileData.put("id", "bbcf20eb-3b2f-42d5-8235-9ef78b54c8a9");
        fileData.put("filename", "69ef9a51-09c1-48f8-920f-580d58552e84");
        fileData.put("filesize", 218220.0);
        fileData.put("checksum", "01f45fa3ff530789a31ed3a2072b51a3");

        // Links
        Map<String, String> links = new HashMap<>();
        links.put("self", "https://sandbox.zenodo.org/api/deposit/depositions/185555/files/bbcf20eb-3b2f-42d5-8235-9ef78b54c8a9");
        links.put("download", "https://sandbox.zenodo.org/api/records/185555/draft/files/69ef9a51-09c1-48f8-920f-580d58552e84/content");

        fileData.put("links", links);

        return fileData;
    }

    private Map<String, Object> mockPublishResponse() {
        Map<String, Object> depositResponse = new HashMap<>();

        // Add the basic information
        depositResponse.put("created", "2025-03-20T09:40:31.680692+00:00");
        depositResponse.put("modified", "2025-03-20T09:40:31.935214+00:00");
        depositResponse.put("id", 185555);
        depositResponse.put("conceptrecid", 185554);
        depositResponse.put("doi", "10.5072/zenodo.185555");
        depositResponse.put("conceptdoi", "10.5072/zenodo.185554");
        depositResponse.put("doi_url", "https://doi.org/10.5072/zenodo.185555");

        // Metadata
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("title", "test/National Infrastructures for Research and Technology -  GRNET S.A/PID Owner (Role)");
        metadata.put("doi", "10.5072/zenodo.185555");
        metadata.put("publication_date", "2025-03-20");
        metadata.put("description", "Publishing assessment test to Zenodo");
        metadata.put("access_right", "open");
        metadata.put("creators", new Object[]{new HashMap<String, Object>() {{
            put("name", "admin admin");
            put("affiliation", null);
        }}});
        metadata.put("license", "cc-zero");
        metadata.put("imprint_publisher", "Zenodo");
        metadata.put("upload_type", "dataset");

        Map<String, Object> prereserveDoi = new HashMap<>();
        prereserveDoi.put("doi", "10.5281/zenodo.185555");
        prereserveDoi.put("recid", 185555);
        metadata.put("prereserve_doi", prereserveDoi);

        depositResponse.put("metadata", metadata);

        // Title
        depositResponse.put("title", "test/National Infrastructures for Research and Technology -  GRNET S.A/PID Owner (Role)");

        // Links
        Map<String, String> links = new HashMap<>();
        links.put("self", "https://sandbox.zenodo.org/api/records/185555");
        links.put("html", "https://sandbox.zenodo.org/records/185555");
        links.put("doi", "https://doi.org/10.5072/zenodo.185555");
        links.put("parent_doi", "https://doi.org/10.5072/zenodo.185554");
        links.put("badge", "https://sandbox.zenodo.org/badge/doi/10.5072%2Fzenodo.185555.svg");
        links.put("conceptbadge", "https://sandbox.zenodo.org/badge/doi/10.5072%2Fzenodo.185554.svg");
        links.put("files", "https://sandbox.zenodo.org/api/records/185555/files");
        links.put("bucket", "https://sandbox.zenodo.org/api/files/53bf72bb-7b63-4840-b5b4-d030cb85eff0");
        links.put("latest_draft", "https://sandbox.zenodo.org/api/deposit/depositions/185555");
        links.put("latest_draft_html", "https://sandbox.zenodo.org/deposit/185555");
        links.put("publish", "https://sandbox.zenodo.org/api/deposit/depositions/185555/actions/publish");
        links.put("edit", "https://sandbox.zenodo.org/api/deposit/depositions/185555/actions/edit");
        links.put("discard", "https://sandbox.zenodo.org/api/deposit/depositions/185555/actions/discard");
        links.put("newversion", "https://sandbox.zenodo.org/api/deposit/depositions/185555/actions/newversion");
        links.put("registerconceptdoi", "https://sandbox.zenodo.org/api/deposit/depositions/185555/actions/registerconceptdoi");
        links.put("record", "https://sandbox.zenodo.org/api/records/185555");
        links.put("record_html", "https://sandbox.zenodo.org/record/185555");
        links.put("latest", "https://sandbox.zenodo.org/api/records/185555/versions/latest");
        links.put("latest_html", "https://sandbox.zenodo.org/record/185555/versions/latest");

        depositResponse.put("links", links);

        // Record ID
        depositResponse.put("record_id", 185555);

        // Owner
        depositResponse.put("owner", 37942);

        // Files
        Map<String, Object> file = new HashMap<>();
        file.put("id", "bbcf20eb-3b2f-42d5-8235-9ef78b54c8a9");
        file.put("filename", "69ef9a51-09c1-48f8-920f-580d58552e84");
        file.put("filesize", 218220);
        file.put("checksum", "01f45fa3ff530789a31ed3a2072b51a3");

        Map<String, String> fileLinks = new HashMap<>();
        fileLinks.put("self", "https://sandbox.zenodo.org/api/records/185555/files/bbcf20eb-3b2f-42d5-8235-9ef78b54c8a9");
        fileLinks.put("download", "https://sandbox.zenodo.org/api/records/185555/draft/files/69ef9a51-09c1-48f8-920f-580d58552e84/content");

        file.put("links", fileLinks);

        depositResponse.put("files", new Object[]{file});

        // State
        depositResponse.put("state", "done");

        // Submitted status
        depositResponse.put("submitted", true);
        return depositResponse;
    }

}

