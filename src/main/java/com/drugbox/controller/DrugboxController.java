package com.drugbox.controller;

import com.drugbox.common.auth.SecurityUtil;
import com.drugbox.dto.request.DrugboxImageChangeRequest;
import com.drugbox.dto.request.DrugboxSaveRequest;
import com.drugbox.dto.response.DrugboxResponse;
import com.drugbox.dto.response.DrugboxSettingResponse;
import com.drugbox.dto.response.IdResponse;
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
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("drugbox")
public class DrugboxController {
    private final DrugboxService drugboxService;

    // 구급상자 추가하기 (생성)
    @PostMapping("/add")
    public ResponseEntity<IdResponse> addDrugbox(@Valid DrugboxSaveRequest request) throws IOException {
        Long drugboxId = drugboxService.addDrugbox(request, SecurityUtil.getCurrentUserId());
        IdResponse response = IdResponse.builder()
                .id(drugboxId)
                .build();
        return ResponseEntity.ok(response);
    }


    // 구급상자 추가하기 (초대)
    @PostMapping("/add/invite-code")
    public ResponseEntity<IdResponse> addDrugboxByInviteCode(@RequestParam(value="inviteCode") String inviteCode) throws IOException {
        Long drugboxId = drugboxService.addDrugboxByInviteCode(inviteCode, SecurityUtil.getCurrentUserId());
        IdResponse response = IdResponse.builder()
                .id(drugboxId)
                .build();
        return ResponseEntity.ok(response);
    }

    // 내 구급상자 리스트 조회
    @GetMapping("/user")
    public ResponseEntity<List<DrugboxResponse>> getUserDrugboxes() {
        List<DrugboxResponse> response = drugboxService.getUserDrugboxes(SecurityUtil.getCurrentUserId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 구급상자 이름 변경하기
    @PatchMapping("/setting/name")
    public ResponseEntity<Void> changeDrugboxName(@RequestParam(value="drugboxId") Long drugboxId,
                                                  @RequestParam(value="name") @NotBlank String name){
        drugboxService.changeDrugboxName(drugboxId, name);
        return new ResponseEntity(HttpStatus.OK);
    }

    // 구급상자 사진 변경하기
    @PatchMapping("/setting/image")
    public ResponseEntity<Void> changeDrugboxImage(@Valid DrugboxImageChangeRequest request) throws IOException {
        drugboxService.changeDrugboxImage(request);
        return new ResponseEntity(HttpStatus.OK);
    }

    // 구급상자 세부설정 조회하기
    @GetMapping("/setting")
    public ResponseEntity<DrugboxSettingResponse> getDrugboxSetting(@RequestParam(value="drugboxId") Long drugboxId) {
        DrugboxSettingResponse response = drugboxService.getDrugboxSetting(drugboxId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 닉네임으로 구급상자 초대하기
    @PostMapping("/invite")
    public ResponseEntity<Void> inviteUserToDrugbox(@RequestParam(value="drugboxId") Long drugboxId,
                                                    @RequestParam(value="nickname") String nickname){
        drugboxService.inviteUserToDrugbox(drugboxId, nickname);
        return new ResponseEntity(HttpStatus.OK);
    }

    // 구급상자 초대 수락하기
    @PostMapping("/invite/accept")
    public ResponseEntity<Void> acceptInvitation(@RequestParam(value="drugboxId") Long drugboxId){
        drugboxService.acceptInvitation(drugboxId, SecurityUtil.getCurrentUserId());
        return new ResponseEntity(HttpStatus.OK);
    }
}
