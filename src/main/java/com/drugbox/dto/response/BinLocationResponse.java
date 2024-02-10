package com.drugbox.dto.response;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class BinLocationResponse {
    private Long id;
    private String x;
    private String y;
    private String address;
    private String addrLvl1; // 시,도
    private String addrLvl2; // 시,군,구
    private String detail;
}
