package com.drugbox.repository;

import com.drugbox.domain.UserDrugbox;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDrugboxRepository extends JpaRepository<UserDrugbox, Long> {
}
