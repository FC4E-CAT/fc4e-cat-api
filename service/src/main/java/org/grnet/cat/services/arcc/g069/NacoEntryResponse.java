package org.grnet.cat.services.arcc.g069;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NacoEntryResponse {

    @JsonProperty("introspection_info")
    private IntrospectionInfo introspectionInfo;

    @JsonProperty("user_info")
    private UserInfo userInfo;

    @JsonProperty("access_token_info")
    private AccessTokenInfo accessTokenInfo;
}
