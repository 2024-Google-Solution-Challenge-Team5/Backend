package com.drugbox.repository;

import com.drugbox.domain.BinLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BinLocationRepository extends JpaRepository<BinLocation, Long> {
}
