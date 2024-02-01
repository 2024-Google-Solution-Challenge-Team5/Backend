package com.drugbox.repository;

import com.drugbox.domain.DrugInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DrugInfoRepository extends JpaRepository<DrugInfo,Long> {
    Optional<DrugInfo> findByName(String name);
}
