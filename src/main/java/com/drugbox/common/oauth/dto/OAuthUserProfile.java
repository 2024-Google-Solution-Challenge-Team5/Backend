package com.drugbox.common.oauth.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuthUserProfile {
    private String email;
    private String nickname;
    private String image;
    private String oauthId;
}
