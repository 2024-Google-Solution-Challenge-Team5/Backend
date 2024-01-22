package com.drugbox.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrugboxSaveRequest {
    private String name;
    private MultipartFile image;
}
