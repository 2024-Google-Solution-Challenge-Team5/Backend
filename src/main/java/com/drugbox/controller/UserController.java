package com.drugbox.controller;

import com.drugbox.dto.response.UserDetailResponse;
import com.drugbox.dto.response.UserEmailResponse;
import com.drugbox.service.UserService;
import com.drugbox.common.auth.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserEmailResponse> findMemberInfoById() {
        return ResponseEntity.ok(userService.findUserInfoById(SecurityUtil.getCurrentUserId()));
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserEmailResponse> findMemberInfoByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.findUserInfoByEmail(email));
    }

    @PostMapping("/reward")
    public ResponseEntity<Void> giveUserRewardPoint(){
        userService.giveUserRewardPoint(SecurityUtil.getCurrentUserId());
        return new ResponseEntity(HttpStatus.OK);
    }

    @PatchMapping("/setting/name")
    public ResponseEntity<Void> changeUserNickname(@RequestParam String nickname){
        userService.changeUserNickname(SecurityUtil.getCurrentUserId(),nickname);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/setting/detail")
    public ResponseEntity<UserDetailResponse> getUserDetail(){
        UserDetailResponse response = userService.getUserDetail(SecurityUtil.getCurrentUserId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/setting/notification/expdate")
    public ResponseEntity<Void> changeIsExpDateNotificationEnabled(@RequestParam boolean isExpDateNotificationEnabled){
        userService.changeIsExpDateNotificationEnabled(SecurityUtil.getCurrentUserId(), isExpDateNotificationEnabled);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PatchMapping("/setting/notification/disposaldrugs")
    public ResponseEntity<Void> changeIsDisposalDrugsNotificationEnabled(@RequestParam boolean isDisposalDrugsNotificationEnabled){
        userService.changeIsDisposalDrugsNotificationEnabled(SecurityUtil.getCurrentUserId(), isDisposalDrugsNotificationEnabled);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PatchMapping("/setting/notification/newannounce")
    public ResponseEntity<Void> changeIsNewAnnounceNotificationEnabled(@RequestParam boolean isNewAnnounceNotificationEnabled){
        userService.changeIsNewAnnounceNotificationEnabled(SecurityUtil.getCurrentUserId(), isNewAnnounceNotificationEnabled);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/setting/reward")
    public ResponseEntity<Integer> getUserRewardPoint(){
        int response = userService.getUserRewardPoints(SecurityUtil.getCurrentUserId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
