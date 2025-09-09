package com.blink.backend.persistence.repository.auth;

import com.blink.backend.persistence.entity.auth.UserClinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserClinicRepository extends JpaRepository<UserClinic, Integer> {

    List<UserClinic> findAllByClinicId(Integer clinicId);

}
