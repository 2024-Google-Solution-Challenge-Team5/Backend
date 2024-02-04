package com.drugbox.service;

import com.drugbox.common.exception.CustomException;
import com.drugbox.common.exception.ErrorCode;
import com.drugbox.common.jwt.TokenDto;
import com.drugbox.common.jwt.TokenProvider;
import com.drugbox.common.oauth.OAuthInfoResponse;
import com.drugbox.common.oauth.OAuthLoginParams;
import com.drugbox.common.oauth.OAuthProvider;
import com.drugbox.common.oauth.RequestOAuthInfoService;
import com.drugbox.common.oauth.dto.OAuthUserProfile;
import com.drugbox.common.oauth.platform.google.GoogleLoginParams;
import com.drugbox.domain.User;
import com.drugbox.dto.request.UserLoginRequest;
import com.drugbox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RequestOAuthInfoService requestOAuthInfoService;

    public TokenDto googleLogin(OAuthLoginParams params){
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        Map<String, Object> idAndIsNew = findOrCreateUser(oAuthInfoResponse);
        Long userId = (Long) idAndIsNew.get("userId");
        Boolean isNewUser = (Boolean) idAndIsNew.get("isNewUser");
        TokenDto token = tokenProvider.generateTokenDto(userId.toString());
        token.setIsNewUser(isNewUser);
        return token;
    }

    private Map<String, Object> findOrCreateUser(OAuthInfoResponse oAuthInfoResponse) {
        OAuthUserProfile profile = tokenProvider.parseIdToken(oAuthInfoResponse.getIdToken());

        Map<String, Object> idAndIfNew = new HashMap<>();
        Boolean isNewUser = false;
        User user = userRepository.findByOauthId(profile.getOauthId());
        if (user == null){
            user = newUser(profile, oAuthInfoResponse.getOAuthProvider());
            isNewUser = true;
        }
        user.setProviderAccessToken(oAuthInfoResponse.getAccessToken());
        userRepository.save(user);

        Long userId = user.getId();
        idAndIfNew.put("userId", userId);
        idAndIfNew.put("isNewUser", isNewUser);
        return idAndIfNew;
    }

    private User newUser(OAuthUserProfile profile, OAuthProvider provider) {
        User user = User.builder()
                .email(profile.getEmail())
                .nickname(profile.getNickname())
                .image(profile.getImage())
                .oauthProvider(provider)
                .oauthId(profile.getOauthId())
                .build();
        return userRepository.save(user);
    }

    public TokenDto getGoogleAccessToken(String authCode){
        System.out.println("\n==== Auth Code:"+authCode+"\n");
        GoogleLoginParams params = new GoogleLoginParams(authCode);
        return googleLogin(params);
    }

    public Long signup(UserLoginRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.EXIST_USER);
        }
        User user = request.toUser(passwordEncoder);
        return userRepository.save(user).getId();
    }

    public TokenDto login(UserLoginRequest request) {
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = request.toAuthentication();

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        //  + Redis에 RefreshToken 저장
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 4. 토큰 발급
        return tokenDto;
    }

    public TokenDto refreshToken(String refreshToken) {
        // 1. Refresh Token 검증
        tokenProvider.validateToken(refreshToken);

        // 2. Token 에서 User ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(refreshToken);

        // 3. Redis에 저장된 Refresh Token과 일치하는지 검사
        tokenProvider.findRefreshToken(refreshToken);

        // 4. 새로운 Access Token 생성
        TokenDto tokenDto = tokenProvider.generateAccessToken(authentication);

        // 토큰 발급
        return tokenDto;
    }

    public void logout(String accessToken){
        tokenProvider.deleteRefreshToken(accessToken);
    }
}
