package com.drugbox.service;

import com.drugbox.domain.Notification;
import com.drugbox.repository.NotificationRepository;
import com.drugbox.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.drugbox.domain.User;
import org.springframework.test.annotation.DirtiesContext;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
@Transactional
public class NotificationServiceTest {

    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    UserRepository userRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @DirtiesContext
    @DisplayName("유저_삭제후_알림_삭제")
    void deleteCascadeNotificationTest() throws Exception {
        // given
        long ucnt = userRepository.count();
        long ncnt = notificationRepository.count();
        User user1 = User.builder().nickname("u1").build();
        userRepository.save(user1);
        Notification notification1 = Notification.builder().user(user1).build();
        notificationRepository.save(notification1);

        // when
        userRepository.deleteById(user1.getId());
        entityManager.flush();
        entityManager.clear();

        // then
        assertThat(userRepository.count()).isEqualTo(ucnt);
        assertThat(notificationRepository.count()).isEqualTo(ncnt);
    }
}
