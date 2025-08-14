package com.blink.backend.persistence.repository;

import com.blink.backend.persistence.entity.message.Chat;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRepository extends CrudRepository<Chat, Integer> {
    @Query("SELECT c.aiAnswer FROM Chat c WHERE c.patient.id = :patientId AND c.clinic.id = :clinicId")
    Boolean findAiAnswerByPatientIdAndClinicId(Integer patientId, Integer clinicId);
    Optional<Chat> findByClinicIdAndPatientPhoneNumber(Integer clinicId, String phoneNumber);

}
