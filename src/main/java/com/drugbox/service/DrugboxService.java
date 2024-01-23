package com.drugbox.service;

import com.drugbox.common.exception.CustomException;
import com.drugbox.common.exception.ErrorCode;
import com.drugbox.domain.UserDrugbox;
import com.drugbox.dto.request.DrugboxSaveRequest;
import com.drugbox.dto.response.DrugboxResponse;
import com.drugbox.repository.DrugboxRepository;
import com.drugbox.domain.User;
import com.drugbox.domain.Drugbox;
import com.drugbox.repository.UserDrugboxRepository;
import com.drugbox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DrugboxService {
    private final DrugboxRepository drugboxRepository;
    private final UserRepository userRepository;
    private final UserDrugboxRepository userDrugboxRepository;
    private final ImageService imageService;


    // 구급상자 추가하기 (생성)
    public Long addDrugbox(DrugboxSaveRequest request) throws IOException {
        User user = getUserOrThrow(request.getUserId());
        String imageUUID = null;
        if (!request.getImage().isEmpty()) {
            imageUUID = imageService.uploadImage(request.getImage());
        }
        Drugbox drugbox = Drugbox.createDrugbox(request.getName(), imageUUID);
        drugboxRepository.save(drugbox);

        UserDrugbox userDrugbox = UserDrugbox.createUserDrugbox(user, drugbox);
        userDrugboxRepository.save(userDrugbox);
        return drugbox.getId();
    }

    // 구급상자 추가하기 (초대)
    public Long addDrugboxByInviteCode(String inviteCode, Long userId){
        User user = getUserOrThrow(userId);
        Drugbox drugbox = drugboxRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DRUGBOX));

        UserDrugbox userDrugbox = UserDrugbox.createUserDrugbox(user, drugbox);
        userDrugboxRepository.save(userDrugbox);
        return drugbox.getId();
    }

    // 내 구급상자 리스트 조회
    public List<DrugboxResponse> getUserDrugboxes(Long userId) {
        getUserOrThrow(userId);
        List<Long> ids = userDrugboxRepository.findDrugboxIdByUserId(userId);
        return ids.stream()
                .map(id-> DrugboxToDrugboxResponse(getDrugboxOrThrow(id)))
                .collect(Collectors.toList());
    }

    // 예외 처리 - 존재하는 User 인가
    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }

    // 예외 처리 - 존재하는 Drugbox 인가
    private Drugbox getDrugboxOrThrow(Long drugboxId) {
        return drugboxRepository.findById(drugboxId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DRUGBOX));
    }

    private DrugboxResponse DrugboxToDrugboxResponse(Drugbox drugbox){
        return DrugboxResponse.builder()
                .name(drugbox.getName())
                .drugboxId(drugbox.getId())
                .inviteCode(drugbox.getInviteCode())
                .image(imageService.processImage(drugbox.getImage()))
                .build();
    }
}
