package com.blink.backend.persistence.repository;

import com.blink.backend.persistence.entity.auth.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByEmail(String email);
    List<Users> findAllByClinicId(Integer clinicId);
}
