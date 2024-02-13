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

    private boolean expDate = true;
    private boolean disposalDrugs = true;
    private boolean newAnnounce = true;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public void setExpDate(boolean expDate) { this.expDate = expDate; }
    public void setDisposalDrugs(boolean disposalDrugs) { this.disposalDrugs = disposalDrugs; }
    public void setNewAnnounce(boolean newAnnounce) { this.newAnnounce = newAnnounce; }

}
