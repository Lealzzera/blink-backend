package com.blink.backend.persistence.repository;

import com.blink.backend.persistence.entity.message.Chat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRepository extends CrudRepository<Chat, Integer> {
    Boolean findIsAiAnswerByPatientIdAndClinicId(Integer patientId, Integer clinicId);
    Optional<Chat> findByClinicIdAndPatientPhoneNumber(Integer clinicId, String phoneNumber);

}
