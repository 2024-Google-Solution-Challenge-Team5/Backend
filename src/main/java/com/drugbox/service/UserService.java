package com.drugbox.service;

import com.drugbox.common.exception.CustomException;
import com.drugbox.common.exception.ErrorCode;
import com.drugbox.domain.NotificationSetting;
import com.drugbox.domain.User;
import com.drugbox.dto.response.UserEmailResponse;
import com.drugbox.repository.NotificationSettingRepository;
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
    private final NotificationSettingRepository notificationSettingRepository;

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

    // 푸시 알림 설정 - 유통기한 알림
   public void changeIsExpDate(Long userId, boolean isExpDate){
        User user = getUserOrThrow(userId);
        NotificationSetting notificationSetting = user.getNotificationSetting();
        notificationSetting.setExpDate(isExpDate);
        userRepository.save(user);
    }

    // 푸시 알림 설정 - 폐기할 의약품 알림
    public void changeIsDisposalDrugs(Long userId, boolean isDisposalDrugs){
        User user = getUserOrThrow(userId);
        NotificationSetting notificationSetting = user.getNotificationSetting();
        notificationSetting.setDisposalDrugs(isDisposalDrugs);
        userRepository.save(user);
    }

    // 푸시 알림 설정 - 새 공지사항
    public void changeIsNewAnnounce(Long userId, boolean isNewAnnounce){
        User user = getUserOrThrow(userId);
        NotificationSetting notificationSetting = user.getNotificationSetting();
        notificationSetting.setNewAnnounce(isNewAnnounce);
        userRepository.save(user);
    }

    // 리워드 포인트 확인
    public int getUserRewardPoints(Long userId){
        User user = getUserOrThrow(userId);
        return user.getPoint();
    }


    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }
}
