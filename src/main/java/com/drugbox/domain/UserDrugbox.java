package com.drugbox.domain;

import com.drugbox.common.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDrugbox extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_drugbox_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "drugbox_id")
    private Drugbox drugbox;

    // 연관관계 메서드
    public void setUser(User user){
        this.user = user;
        user.getUserDrugboxes().add(this);
    }

    public void setDrugbox(Drugbox drugbox){
        this.drugbox = drugbox;
        drugbox.getUserDrugboxes().add(this);
    }

    // 생성 메서드
    public static UserDrugbox createUserDrugbox(User user, Drugbox drugbox) {
        UserDrugbox userDrugbox = new UserDrugbox();
        userDrugbox.setUser(user);
        userDrugbox.setDrugbox(drugbox);
        return userDrugbox;
    }
}
