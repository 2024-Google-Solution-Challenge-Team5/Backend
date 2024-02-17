package com.drugbox.dto.response;

import lombok.Builder;

@Builder
public class MapDetailResponse {
    private String locationName;
    private String locationId;
    private String locationPhotos;
    private String formattedAddress;
    private String currentOpeningHours;
}
