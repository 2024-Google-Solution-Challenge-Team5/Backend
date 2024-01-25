package com.drugbox.controller;

import com.drugbox.dto.request.DrugboxSaveRequest;
import com.drugbox.dto.response.DrugboxResponse;
import com.drugbox.service.DrugboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("drugbox")
public class DrugboxController {
    private final DrugboxService drugboxService;

    // 구급상자 추가하기 (생성)
    @PostMapping("/add")
    public ResponseEntity<Map<String, Long>> addDrugbox(@Valid DrugboxSaveRequest request) throws IOException {
        Long drugboxId = drugboxService.addDrugbox(request);
        Map<String, Long> response = new HashMap<>();
        response.put("drugboxId", drugboxId);
        return ResponseEntity.ok(response);
    }


    // 구급상자 추가하기 (초대)
    @PostMapping("/add/invite-code")
    public ResponseEntity<Map<String, Long>> addDrugboxByInviteCode(@RequestParam(value="inviteCode") String inviteCode,
                                                                    @RequestParam(value="userId") Long userId) throws IOException {
        Long drugboxId = drugboxService.addDrugboxByInviteCode(inviteCode, userId);
        Map<String, Long> response = new HashMap<>();
        response.put("drugboxId", drugboxId);
        return ResponseEntity.ok(response);
    }

    // 내 구급상자 리스트 조회
    @GetMapping("/user")
    public ResponseEntity<List<DrugboxResponse>> getUserDrugboxes(@RequestParam(value="userId") Long userId) {
        List<DrugboxResponse> response = drugboxService.getUserDrugboxes(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 구급상자 이름 변경하기
    @PatchMapping("/setting/name")
    public ResponseEntity<Void> changeDrugboxName(@RequestParam(value="drugboxId") Long drugboxId,
                                                  @RequestParam(value="name") @NotBlank String name){
        drugboxService.changeDrugboxName(drugboxId, name);
        return new ResponseEntity(HttpStatus.OK);
    }
}
