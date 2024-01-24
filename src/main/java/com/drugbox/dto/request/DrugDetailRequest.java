package com.drugbox.dto.request;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DrugDetailRequest {
    private int count;
    private String location;
    private LocalDate expDate;
}
