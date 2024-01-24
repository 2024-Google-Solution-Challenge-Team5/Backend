package com.drugbox.dto.response;

import com.drugbox.domain.Drugbox;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class DrugResponse {
    private Long id;

    private String name;
    private int count;
    private String location;
    private LocalDate expDate;
}
