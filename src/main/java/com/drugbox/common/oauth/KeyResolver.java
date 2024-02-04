package com.drugbox.common.oauth;

import io.jsonwebtoken.SigningKeyResolverAdapter;

import java.net.URL;
import java.security.Key;
import java.security.PublicKey;
import java.util.List;

public class KeyResolver extends SigningKeyResolverAdapter {
    private final List<PublicKey> publicKeys;

    public KeyResolver(List<PublicKey> publicKeys) {
        this.publicKeys = publicKeys;
    }

    public Key resolveSigningKey(io.jsonwebtoken.Header header, io.jsonwebtoken.Claims claims) {
        return publicKeys.get(0);  // 첫 번째 공개 키를 사용
    }
}
