package org.grnet.cat.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AarcClaimLocation {

    @JsonProperty("user_info")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public ArccValidationResult userInfo;

    @JsonProperty("introspection_info")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public ArccValidationResult introspectionInfo;

    @JsonProperty("access_token_info")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public ArccValidationResult accessTokenInfo;

    @JsonProperty("id_token")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public ArccValidationResult idToken;
}
