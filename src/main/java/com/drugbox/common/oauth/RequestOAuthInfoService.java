package com.drugbox.common.oauth;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class RequestOAuthInfoService { // OAuthApiClient 를 사용하는 Service 클래스

    private final Map<OAuthProvider, OAuthApiClient> clients;

    public RequestOAuthInfoService(List<OAuthApiClient> clients) {
        this.clients = clients.stream().collect(
                Collectors.toUnmodifiableMap(OAuthApiClient::oAuthProvider, Function.identity())
        );
    }

    public OAuthInfoResponse request(OAuthLoginParams params) {
        OAuthApiClient client = clients.get(params.oAuthProvider());
        return client.requestAccessToken(params);
    }

    public void quit(String accessToken, OAuthProvider oauthProvider){
        OAuthApiClient client = clients.get(oauthProvider);
        client.quit(accessToken);
    }
}
