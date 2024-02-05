package com.drugbox.service;

import com.drugbox.domain.User;
import com.drugbox.domain.Notification;
import com.drugbox.repository.NotificationRepository;
import com.drugbox.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class NotificationServiceTest {

    @Autowired NotificationRepository notificationRepository;
    @Autowired UserRepository userRepository;
    @Autowired EntityManager entityManager;

    @Test
    public void deleteCascadeNotificationTest() throws Exception {
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
