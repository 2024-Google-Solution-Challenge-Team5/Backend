package com.drugbox.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class DrugListResponse {
    private String location;
    private int count;
    private LocalDate expDate;
}
