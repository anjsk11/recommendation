package com.sensingbros.recommendation.repository;

import com.sensingbros.recommendation.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UsersRepository extends JpaRepository<Users, UUID> {
    // 기본 CRUD 기능은 모두 자동으로 제공됨
}