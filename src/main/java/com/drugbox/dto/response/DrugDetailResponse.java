package com.drugbox.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
public class DrugDetailResponse {
    private String name;
    private List<DrugResponse> drugResponseList;
    private String effect;
}
