package com.blink.backend.persistence.repository;

import com.blink.backend.persistence.entity.auth.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Integer> {
    List<UserEntity> findAllByClinicId(Integer clinicId);

    Optional<UserEntity> findByUserId(UUID userId);
}
