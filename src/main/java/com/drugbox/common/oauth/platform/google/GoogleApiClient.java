package com.drugbox.common.oauth.platform.google;

import com.drugbox.common.exception.CustomException;
import com.drugbox.common.exception.ErrorCode;
import com.drugbox.common.oauth.OAuthApiClient;
import com.drugbox.common.oauth.OAuthLoginParams;
import com.drugbox.common.oauth.OAuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Component
@RequiredArgsConstructor
public class GoogleApiClient implements OAuthApiClient { // 구글 로그인 토큰 받기 & 사용자 정보 가져오기

    private static final String GRANT_TYPE = "authorization_code";

    private String authUrl = "https://oauth2.googleapis.com";
    private String apiUrl = "https://www.googleapis.com/drive/v2/files";

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    private final RestTemplate restTemplate; // 외부 요청 후 미리 정의해둔 GoogleTokens, GoogleInfoResponse 로 응답값을 받는다

    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.GOOGLE;
    }

    @Override
    public GoogleInfoResponse requestAccessToken(OAuthLoginParams params) {
        String url = authUrl + "/token";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, Object> body = params.makeBody();
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        GoogleTokens response = restTemplate.postForObject(url, request, GoogleTokens.class);

        assert response != null;
        System.out.println("\n==== access_token:"+response.getAccessToken());
        System.out.println("==== socpe:"+response.getScope());
        System.out.println("==== id_token:"+response.getIdToken());
        System.out.println("==== token_type:"+response.getTokenType());

        GoogleInfoResponse ret = new GoogleInfoResponse(response.getAccessToken(), response.getIdToken());
        return ret;
    }

    @Override
    public void quit(String accessToken){ // 구글에서 발급한 accessToken revoke 신청
        String url = authUrl + "/revoke?token=" + accessToken;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request,String.class);
        if(response.getStatusCode() != HttpStatus.OK){
            throw new CustomException(ErrorCode.QUIT_ERROR);
        }
    }
}
