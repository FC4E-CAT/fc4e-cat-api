package org.grnet.cat.enums;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.grnet.cat.exceptions.EntityNotFoundException;
import org.grnet.cat.exceptions.InternalServerErrorException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 *
 * Enumeration of Integration Sources
 */
public enum Source {

    ROR("ror", "ROR", "https://api.ror.org/organizations"),
    EOSC("eosc", "EOSC", "http://api.eosc-portal.eu/provider"),
    RE3DATA("re3data", "RE3DATA", "");

    public final String label;
    public final String organisationSource;
    public final String url;

    Source(String label, String organisationSource, String url) {
        this.label = label;
        this.organisationSource = organisationSource;
        this.url = url;
    }

    public String getLabel() {
        return label;

    }

    public String getOrganisationSource() {
        return organisationSource;
    }

    public String getUrl() {
        return url;
    }

    public Source getEnumNameForValue(String value) {
        List<Source> values = Arrays.asList(Source.values());
        for (Source op : values) {

            if (op.getLabel().equalsIgnoreCase(value)) {
                return op;
            }
        }
        return null;
    }

    public String connectHttpClient(String id) throws IOException {

        var client = new OkHttpClient().newBuilder()
                .build();

        var request = new Request.Builder()
                .url(url + "/" + id)
                .method("GET", null)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        Response resp = client.newCall(request).execute();

        if (resp.code() == 404) {
            throw new EntityNotFoundException("Organisation " + id + ", not found in " + organisationSource);
        } else if (resp.code() != 200) {
            throw new InternalServerErrorException("Internal Server Error");
        }

        return resp.body().string();
    }
}
