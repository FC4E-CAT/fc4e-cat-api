package org.grnet.cat.entities;

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
}
