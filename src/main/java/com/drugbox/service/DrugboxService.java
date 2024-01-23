package com.drugbox.service;

import com.drugbox.common.exception.CustomException;
import com.drugbox.common.exception.ErrorCode;
import com.drugbox.domain.UserDrugbox;
import com.drugbox.dto.request.DrugboxSaveRequest;
import com.drugbox.repository.DrugboxRepository;
import com.drugbox.domain.User;
import com.drugbox.domain.Drugbox;
import com.drugbox.repository.UserDrugboxRepository;
import com.drugbox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.UUID;

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

    // 예외 처리 - 존재하는 User 인가
    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }
}
