package com.blink.backend.persistence.repository;

import com.blink.backend.persistence.entity.auth.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {


}
