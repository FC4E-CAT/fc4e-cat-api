package org.grnet.cat.entities;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Organisation {
    
    public String id;
    public String name;
    public String website;

    public Organisation(String id, String name, String website) {
        this.id = id;
        this.name = name;
        this.website = website;
    }
    
    

    public String getWebsite() {
        return website;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    
    public static Organisation buildFromString(String content) {

        JsonParser jsonParser = new JsonParser();
        // Grab the first - and only line of json from ops data
        JsonElement jElement = jsonParser.parse(content);

        JsonObject jRoot = jElement.getAsJsonObject();
        String id = jRoot.get("id").getAsString();
        id = id.replaceAll("ttps://ror.org/", "");

        String name = jRoot.get("name").getAsString();
        String website = null;
        if (jRoot.has("links")) {
            website = jRoot.get("links").getAsJsonArray().get(0).getAsString();

        } else if (jRoot.has("website")) {
            website = jRoot.get("website").getAsString();

        }

        Organisation org = new Organisation(id, name, website);
        return org;
    }


    
}
