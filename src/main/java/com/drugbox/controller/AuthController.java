package com.drugbox.controller;

import com.drugbox.common.jwt.TokenDto;
import com.drugbox.dto.request.UserLoginRequest;
import com.drugbox.dto.response.IdResponse;
import com.drugbox.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<IdResponse> signup(@RequestBody UserLoginRequest userLoginRequest) {
        Long userId = authService.signup(userLoginRequest);
        IdResponse response = IdResponse.builder()
                .id(userId)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody UserLoginRequest userLoginRequest) {
        return ResponseEntity.ok(authService.login(userLoginRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenDto> refreshToken(@RequestBody Map<String, String> refreshToken) {
        return ResponseEntity.ok(authService.refreshToken(refreshToken.get("refreshToken")));
    }
}
