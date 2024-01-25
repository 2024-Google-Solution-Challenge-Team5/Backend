package com.drugbox.controller;

import com.drugbox.dto.request.DrugRequest;
import com.drugbox.dto.response.DrugResponse;
import com.drugbox.service.DrugService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @PostMapping("/add")
    public ResponseEntity<Map<String,List<Long>>> addDrug(@Valid DrugRequest drugRequest){
        List<Long> ids = drugService.addDrug(drugRequest);
        Map<String, List<Long>> response = new HashMap<>();
        response.put("drugids",ids);
        return ResponseEntity.ok(response);
    }
}
