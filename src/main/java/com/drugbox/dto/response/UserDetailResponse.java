package com.drugbox.dto.response;

import lombok.Builder;

@Builder
public class UserDetailResponse {
    private String nickname;
    private String email;
}
