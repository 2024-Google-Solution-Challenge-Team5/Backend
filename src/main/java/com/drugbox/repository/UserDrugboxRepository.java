package com.drugbox.repository;

import com.drugbox.domain.UserDrugbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserDrugboxRepository extends JpaRepository<UserDrugbox, Long> {
    @Query(value="SELECT ud.drugbox_id FROM user_drugbox ud WHERE ud.user_id= :userId", nativeQuery = true)
    List<Long> findDrugboxIdByUserId(@Param("userId") Long userId);
}
