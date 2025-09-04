package org.grnet.cat.enums;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.grnet.cat.exceptions.EntityNotFoundException;
import org.grnet.cat.exceptions.InternalServerErrorException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Enumeration of Integration Sources
 */
public enum Source {

    ROR("ror", "ROR", "https://api.ror.org/v2/organizations", true) {
        public RorSearchInfo execute(String query, int page) {
            Response resp = connectHttpClient(url + "?query=" + query + "&page=" + page, query);
            try {
                return buildOrgsInfo(resp.body().string(), query);
            } catch (IOException ex) {
                Logger.getLogger(Source.class.getName()).log(Level.SEVERE, "null response body", ex);
            }
            return null;
        }

        public String[] execute(String query) {
            try {
                Response resp = connectHttpClient(url + "/" + query, query);
                return buildOrgInfo(resp.body().string());
            } catch (IOException ex) {
                Logger.getLogger(Source.class.getName()).log(Level.SEVERE, "null response body", ex);
            }
            return null;
        }


    },
    EOSC("eosc", "EOSC", "http://api.eosc-portal.eu/provider", false) {
        public String[] execute(String query) {

            throw new InternalServerErrorException("Source EOSC is not supported.", 501);
        }

        @Override
        public RorSearchInfo execute(String query, int page) {
            throw new InternalServerErrorException("Source EOSC is not supported.", 501);
        }

    },
    RE3DATA("re3data", "RE3DATA", "", false) {
        public String[] execute(String query) {
            throw new InternalServerErrorException("Source RE3DATA is not supported.", 501);
        }

        @Override
        public RorSearchInfo execute(String query, int page) {
            throw new InternalServerErrorException("Source RE3DATA is not supported.", 501);
        }

    },

    CUSTOM("custom", "CUSTOM", "", true) {
        @Override
        public String[] execute(String query) {
            return new String[0];
        }

        @Override
        public RorSearchInfo execute(String query, int page) {
            return null;
        }

    },

    NACO("naco", "NACO", "", true) {
        @Override
        public RorSearchInfo execute(String query, int page) {
            return null;
        }

        @Override
        public String[] execute(String query) {
            return new String[0];
        }
    };

    public abstract String[] execute(String query);

    public abstract RorSearchInfo execute(String query, int page);

    @Getter
    public final String label;
    @Getter
    public final String organisationSource;
    @Getter
    public final String url;
    public final boolean enabled;

    Source(String label, String organisationSource, String url, boolean enabled) {
        this.label = label;
        this.organisationSource = organisationSource;
        this.url = url;
        this.enabled = enabled;
    }

    public static List<Source> getAvailableSources() {

        return Arrays.stream(Source.values()).filter(source -> source.enabled).collect(Collectors.toList());
    }


    @SneakyThrows
    Response connectHttpClient(String url, String identifier) {
        var client = new OkHttpClient().newBuilder()
                .build();
        var request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        Response resp = null;
        try {
            resp = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (resp.code() == 404) {
            throw new EntityNotFoundException("Organisation " + identifier + ", not found in " + organisationSource);
        } else if (resp.code() != 200) {
            throw new InternalServerErrorException("Cannot communicate with " + organisationSource, 500);
        }
        return resp;
    }

    @SneakyThrows
    Response connectEndpointHttpClient(String url, String token, String identifier) {
        var client = new OkHttpClient().newBuilder()
                .build();
        var request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .addHeader("Authorization", "Bearer " + token)
                .build();

        Response resp = null;
        try {
            resp = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (resp.code() == 404) {
            throw new EntityNotFoundException("Organisation " + identifier + ", not found in " + organisationSource);
        } else if (resp.code() != 200) {
            throw new InternalServerErrorException("Cannot communicate with " + organisationSource, 500);
        }
        return resp;
    }

    String[] buildOrgInfo(String content) {

        JsonParser jsonParser = new JsonParser();
        // Grab the first - and only line of json from ops data
        JsonElement jElement = jsonParser.parse(content);

        JsonObject jRoot = jElement.getAsJsonObject();
        return returnOrgInfo(jRoot);
    }

    RorSearchInfo buildOrgsInfo(String content, String query) {

        JsonParser jsonParser = new JsonParser();
        JsonElement jElement = jsonParser.parse(content);
        JsonObject jRoot = jElement.getAsJsonObject();


        int total = jRoot.get("number_of_results").getAsInt();
        if (total == 0) {
            throw new EntityNotFoundException("Organisation " + query + ", not found in " + organisationSource);

        }
        JsonArray items = jRoot.get("items").getAsJsonArray();

        List<String[]> list = new ArrayList<>();

        Iterator iter = items.iterator();
        while (iter.hasNext()) {
            JsonObject item = (JsonObject) iter.next();
            list.add(returnOrgInfo(item));
        }
        return new RorSearchInfo(total, list);
    }

    private String[] returnOrgInfo(JsonObject jRoot) {

        if (jRoot == null || !jRoot.has("id") || !jRoot.has("names")) {
            throw new EntityNotFoundException("Missing expected fields in the organisation data.");
        }

        var id = jRoot.get("id").getAsString();
        if (id == null || id.isEmpty()) {
            throw new EntityNotFoundException("Organisation ID is missing or empty.");
        }
        id = id.replaceAll("https://ror.org/", "");


        // Extract name from names[0].value
        String name = null;
        var namesArray = jRoot.getAsJsonArray("names");
        if (namesArray != null && !namesArray.isEmpty()) {
            for (JsonElement nameElement : namesArray) {
                JsonObject nameObj = nameElement.getAsJsonObject();
                if (nameObj.has("types")) {
                    JsonArray types = nameObj.getAsJsonArray("types");
                    for (JsonElement type : types) {
                        if ("ror_display".equalsIgnoreCase(type.getAsString()) && nameObj.has("value")) {
                            name = nameObj.get("value").getAsString();
                            break;
                        }
                    }
                }
                if (name != null) break;
            }
        }

        if (name == null || name.isEmpty()) {
            throw new EntityNotFoundException("Organisation name is missing or empty.");
        }

        // Extract website from links[].value where type is homepage (fallback to first)
        String website = null;
        if (jRoot.has("links")) {
            JsonArray linksArray = jRoot.getAsJsonArray("links");
            for (JsonElement linkEl : linksArray) {
                JsonObject linkObj = linkEl.getAsJsonObject();
                if (linkObj.has("type") && linkObj.get("type").getAsString().equals("homepage")) {
                    website = linkObj.get("value").getAsString();
                    break;
                }
            }
            if (website == null && !linksArray.isEmpty()) {
                JsonObject firstLink = linksArray.get(0).getAsJsonObject();
                if (firstLink.has("value")) {
                    website = firstLink.get("value").getAsString();
                }
            }
        }

        // Extract acronym (optional)
        String acronym = null;
        if (!namesArray.isEmpty()) {
            for (JsonElement nameElement : namesArray) {
                JsonObject nameObj = nameElement.getAsJsonObject();

                if (nameObj.has("types")) {
                    JsonArray types = nameObj.getAsJsonArray("types");
                    for (JsonElement type : types) {
                        if ("acronym".equalsIgnoreCase(type.getAsString())) {
                            if (nameObj.has("value")) {
                                acronym = nameObj.get("value").getAsString();
                                break;
                            }
                        }
                    }
                }

                if (acronym != null) {
                    break;
                }
            }
        }


        return new String[]{id, name, website, acronym};
    }

    public class RorSearchInfo {

        int total;
        List<String[]> orgElements;

        public RorSearchInfo(int total, List<String[]> orgElements) {
            this.total = total;
            this.orgElements = orgElements;
        }

        public int getTotal() {
            return total;
        }

        public List<String[]> getOrgElements() {
            return orgElements;
        }

    }
}
