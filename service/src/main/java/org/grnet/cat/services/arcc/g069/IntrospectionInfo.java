package org.grnet.cat.services.arcc.g069;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class IntrospectionInfo {

    @JsonProperty("entitlements")
    private List<String> entitlements;

    @JsonProperty("sub")
    private String sub;

    @JsonProperty("voperson_id")
    @JsonDeserialize(using = StringOrArrayDeserializer.class)
    private List<String> vopersonId;

    @JsonProperty("schac_home_organization")
    private String organizationDomain;

    @JsonProperty("voperson_external_affiliation")
    private List<String> affiliationWithHomeOrganization;

    @JsonProperty("eduperson_assurance")
    private List<String> assurance;
}
