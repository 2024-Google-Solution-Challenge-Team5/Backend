package com.drugbox.common.oauth;


public interface OAuthApiClient {
    OAuthProvider oAuthProvider(); // Client 의 타입 반환
    OAuthInfoResponse requestAccessToken(OAuthLoginParams params); // Authorization Code 를 기반으로 인증 API 를 요청해서 Access Token 을 획득
    void quit(String accessToken);
}
