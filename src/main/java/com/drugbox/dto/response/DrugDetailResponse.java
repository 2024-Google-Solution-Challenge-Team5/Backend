package com.drugbox.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
public class DrugDetailResponse {
    private String name;
    private List<DrugListResponse> drugListResponseList;
    private String effect;
}
