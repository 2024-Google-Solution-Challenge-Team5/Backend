package com.drugbox.domain;

import com.drugbox.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSetting extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notificationsetting_id")
    private Long id;

    private boolean isExpDateNotificationEnabled = true;
    private boolean isDisposalDrugsNotificationEnabled = true;
    private boolean isNewAnnounceNotificationEnabled = true;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public void setIsExpDateNotificationEnabled(boolean expDate) { this.isExpDateNotificationEnabled = expDate; }
    public void setIsDisposalDrugsNotificationEnabled(boolean disposalDrugs) { this.isDisposalDrugsNotificationEnabled = disposalDrugs; }
    public void setIsNewAnnounceNotificationEnabled(boolean newAnnounce) { this.isNewAnnounceNotificationEnabled = newAnnounce; }

}
