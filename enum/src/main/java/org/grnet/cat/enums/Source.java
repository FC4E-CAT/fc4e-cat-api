package org.grnet.cat.enums;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.grnet.cat.exceptions.EntityNotFoundException;
import org.grnet.cat.exceptions.InternalServerErrorException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Enumeration of Integration Sources
 */
public enum Source {

    ROR("ror", "ROR", "https://api.ror.org/organizations") {
        public Map<String, String> execute(String id) {
            return connectHttpClient(id);
        }
    },
    EOSC("eosc", "EOSC", "http://api.eosc-portal.eu/provider") {
        public Map<String, String> execute(String id) {
            return connectHttpClient(id);
        }
    },
    RE3DATA("re3data", "RE3DATA", "") {
        public Map<String, String> execute(String id) {
            throw new InternalServerErrorException("Source re3data is not supported.", 501);
        }
    };

    public abstract Map<String, String> execute(String id);

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

    @SneakyThrows
    Map<String, String> connectHttpClient(String id) {

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
            throw new InternalServerErrorException("Cannot communicate with " + organisationSource, 500);
        }

        return buildFromString(resp.body().string());
    }

    Map<String, String> buildFromString(String content) {

        JsonParser jsonParser = new JsonParser();
        // Grab the first - and only line of json from ops data
        JsonElement jElement = jsonParser.parse(content);

        JsonObject jRoot = jElement.getAsJsonObject();
        String id = jRoot.get("id").getAsString();
        id = id.replaceAll("https://ror.org/", "");

        String name = jRoot.get("name").getAsString();
        String website = null;
        if (jRoot.has("links")) {
            website = jRoot.get("links").getAsJsonArray().get(0).getAsString();

        } else if (jRoot.has("website")) {
            website = jRoot.get("website").getAsString();

        }

        var map = new HashMap<String, String>();

        map.put("id", id);
        map.put("name", name);
        map.put("website", website);

        return map;
    }
}
