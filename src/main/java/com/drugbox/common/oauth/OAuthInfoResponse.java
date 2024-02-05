package com.drugbox.common.oauth;

public interface OAuthInfoResponse {
    OAuthProvider getOAuthProvider();
    String getIdToken();
    String getAccessToken();
}
