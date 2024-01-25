package com.drugbox.repository;

import com.drugbox.domain.Drugbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DrugboxRepository extends JpaRepository<Drugbox, Long> {
    Optional<Drugbox> findByInviteCode(String inviteCode);
}
