package org.grnet.cat.services.arcc.g069;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class IntrospectionInfo {

    @JsonProperty("entitlements")
    private List<String> entitlements;

    public List<String> getEntitlements() {
        return entitlements;
    }

    public void setEntitlements(List<String> entitlements) {
        this.entitlements = entitlements;
    }
}
