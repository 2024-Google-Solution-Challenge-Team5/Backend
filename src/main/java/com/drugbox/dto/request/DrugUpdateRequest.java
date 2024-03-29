package com.drugbox.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrugUpdateRequest {
    private Long drugboxId;
    private List<Long> drugIds;
}
