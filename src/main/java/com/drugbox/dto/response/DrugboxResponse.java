package com.drugbox.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DrugboxResponse {
    private String name;
    private Long drugboxId;
    private String image;
    private String inviteCode;
}
