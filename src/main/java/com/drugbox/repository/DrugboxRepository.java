package com.drugbox.repository;

import com.drugbox.domain.Drugbox;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrugboxRepository extends JpaRepository<Drugbox, Long> {
}
