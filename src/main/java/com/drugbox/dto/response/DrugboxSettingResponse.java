package com.drugbox.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DrugboxSettingResponse {
    private String name;
    private Long drugboxId;
    private String image;
    private String inviteCode;
    private List<UserResponse> users;
}
