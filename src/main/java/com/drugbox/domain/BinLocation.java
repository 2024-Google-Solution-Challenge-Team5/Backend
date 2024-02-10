package com.drugbox.domain;

import com.drugbox.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BinLocation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "binlocation_id")
    private Long id;

    @Column(nullable = false)
    private String x;
    @Column(nullable = false)
    private String y;
    @Column(nullable = false)
    private String address;
    private String addrLvl1; // 시,도
    private String addrLvl2; // 시,군,구
    private String detail;
}
