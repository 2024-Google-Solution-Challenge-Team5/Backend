package com.drugbox.service;

import com.drugbox.common.exception.CustomException;
import com.drugbox.common.exception.ErrorCode;
import com.drugbox.domain.User;
import com.drugbox.dto.response.UserEmailResponse;
import com.drugbox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserEmailResponse findUserInfoById(Long userId){
        return userRepository.findById(userId)
                .map(UserEmailResponse::of)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_USER));
    }

    public UserEmailResponse findUserInfoByEmail(String email){
        return userRepository.findByEmail(email)
                .map(UserEmailResponse::of)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_USER));
    }

    public void giveUserRewardPoint(Long userId){
        User user = getUserOrThrow(userId);
        user.add100Point();
    }

    // 유저 이름 변경
    public void changeUserNickname(Long userId, String nickname){
        User user = getUserOrThrow(userId);
        user.setNickname(nickname);
        userRepository.save(user);
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }
}
