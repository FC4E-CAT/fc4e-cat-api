package org.grnet.cat.services.arcc.g069;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AccessTokenInfo {

    @JsonProperty("sub")
    private String sub;

    @JsonProperty("voperson_id")
    @JsonDeserialize(using = StringOrArrayDeserializer.class)
    private List<String> vopersonId;

    @JsonProperty("eduperson_assurance")
    private List<String> assurance;
}
