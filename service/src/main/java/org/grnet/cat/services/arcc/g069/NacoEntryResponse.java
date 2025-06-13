package org.grnet.cat.services.arcc.g069;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NacoEntryResponse {

    @JsonProperty("introspection_info")
    private IntrospectionInfo introspectionInfo;

    @JsonProperty("user_info")
    private UserInfo userInfo;

    public IntrospectionInfo getIntrospectionInfo() {
        return introspectionInfo;
    }

    public void setIntrospectionInfo(IntrospectionInfo introspectionInfo) {
        this.introspectionInfo = introspectionInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
