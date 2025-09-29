package org.grnet.cat.converter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterDefinition {

    private String name;
    private String type;
    private Boolean required;

    public FilterDefinition() {} // no-args constructor

    // getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Boolean getRequired() { return required; }
    public void setRequired(Boolean required) { this.required = required; }

}