package com.sensingbros.recommendation.repository;

import com.sensingbros.recommendation.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Integer> {
    // 기본 CRUD 기능은 모두 자동으로 제공됨
}