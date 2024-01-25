package com.drugbox.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrugboxImageChangeRequest {
    @NotNull
    private Long drugboxId;
    private MultipartFile image;
}
