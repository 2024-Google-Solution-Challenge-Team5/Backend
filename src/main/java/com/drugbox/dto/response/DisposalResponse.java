package com.drugbox.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DisposalResponse {
    private Long drugboxId;
    private String drugboxName;
    private List<DrugResponse> drugResponses;
}
