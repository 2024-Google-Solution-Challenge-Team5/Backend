package com.drugbox.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {
    private String nickname;
    private Long userId;
}
