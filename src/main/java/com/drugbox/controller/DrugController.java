package com.drugbox.controller;

import com.drugbox.dto.response.DrugResponse;
import com.drugbox.service.DrugService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("drugs")
public class DrugController {
    private final DrugService drugService;

    @GetMapping("/list")
    public ResponseEntity<List<DrugResponse>> getDrugs(@RequestParam(value="drugboxId") Long drugboxId) {
        List<DrugResponse> response = drugService.getDrugList(drugboxId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
