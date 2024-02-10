package com.drugbox.repository;

import com.drugbox.domain.BinLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BinLocationRepository extends JpaRepository<BinLocation, Long> {
    List<BinLocation> findAllByAddrLvl1AndAddrLvl2(String addrLvl1, String addrLvl2);
    List<BinLocation> findAllByAddrLvl1(String addrLvl1);
}
