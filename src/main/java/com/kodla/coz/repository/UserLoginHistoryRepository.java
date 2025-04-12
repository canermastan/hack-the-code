package com.kodla.coz.repository;

import com.kodla.coz.model.UserLoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserLoginHistoryRepository extends JpaRepository<UserLoginHistory, Integer> {
    Optional<UserLoginHistory> findByUserId(Integer userId);
}
