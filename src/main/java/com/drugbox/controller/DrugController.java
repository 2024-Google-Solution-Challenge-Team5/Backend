package com.drugbox.controller;

import com.drugbox.dto.request.DrugRequest;
import com.drugbox.dto.request.DrugUseRequest;
import com.drugbox.dto.response.DrugDetailResponse;
import com.drugbox.dto.response.DrugResponse;
import com.drugbox.dto.response.IdResponse;
import com.drugbox.service.DrugService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<List<IdResponse>> addDrug(@RequestBody @Valid DrugRequest drugRequest) throws IOException, ParseException {
        List<Long> ids = drugService.addDrug(drugRequest);
        List<IdResponse> response = ids.stream()
                .map(id -> IdResponse.builder().id(id).build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/use")
    public ResponseEntity<Void> useDrugs(List<DrugUseRequest> drugUseRequests){
        drugService.useDrug(drugUseRequests);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PatchMapping("/dispose")
    public ResponseEntity<Void> disposeDrug(@RequestParam(value="drugboxId") Long drugboxId,
                                           @RequestParam(value="drugId") Long drugId){
        drugService.disposeDrug(drugboxId,drugId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/detail")
    public ResponseEntity<DrugDetailResponse> getDrugDetail(@RequestParam(value="drugboxId") Long drugboxId,
                                              @RequestParam(value="name") String name) throws IOException, ParseException {
        DrugDetailResponse response = drugService.getDrugDetail(drugboxId,name);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
