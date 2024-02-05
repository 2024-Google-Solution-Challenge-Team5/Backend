package com.drugbox.common.oauth.platform.google;

import com.drugbox.common.oauth.OAuthInfoResponse;
import com.drugbox.common.oauth.OAuthProvider;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleInfoResponse implements OAuthInfoResponse {

    private String accessToken;
    private String idToken;

    public GoogleInfoResponse(String accessToken, String idToken) {
        this.accessToken = accessToken;
        this.idToken = idToken;
    }


    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.GOOGLE;
    }

    @Override
    public String getIdToken(){ return idToken; }

    @Override
    public String getAccessToken(){ return accessToken; }
}
