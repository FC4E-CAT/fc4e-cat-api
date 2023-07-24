package org.grnet.cat.entities;

public class Organisation {
    
    public String id;
    public String name;
    public String website;

    public String acronym;

    public Organisation(String id, String name, String website, String acronym) {
        this.id = id;
        this.name = name;
        this.website = website;
        this.acronym = acronym;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }
}