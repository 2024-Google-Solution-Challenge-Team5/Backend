package com.drugbox.controller;

import com.drugbox.dto.request.DrugboxSaveRequest;
import com.drugbox.service.DrugboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
}
