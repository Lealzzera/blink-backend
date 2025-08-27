package com.blink.backend.domain.service;

import com.blink.backend.controller.message.dto.ChatHistoryDto;
import com.blink.backend.controller.message.dto.ChatOverviewDto;
import com.blink.backend.domain.exception.NotFoundException;
import com.blink.backend.domain.exception.message.WhatsAppNotConnectedException;
import com.blink.backend.persistence.entity.appointment.Patient;
import com.blink.backend.persistence.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class ChatConfigurationService {
    private final PatientRepository patientRepository;
    private final WahaService wahaService;

    public Boolean toggleAiAnswerMode(Integer clinicId, String phoneNumber) throws NotFoundException {
        Patient patient = patientRepository.findByClinic_IdAndPhoneNumber(clinicId, phoneNumber)
                .orElseThrow(() -> new NotFoundException("Paciente"));
        patient.setAiAnswer(patient.getAiAnswer());
        patientRepository.save(patient);
        return patient.getAiAnswer();
    }

    public List<ChatOverviewDto> getChatOverView(Integer clinicId, Integer page)
            throws NotFoundException, WhatsAppNotConnectedException {
        log.info("init-get-chat-overview, clinicId={}", clinicId);
        List<ChatOverviewDto> response = wahaService.getChatsOverview(clinicId, page);
        log.info("end-get-chat-overview, clinicId={}", clinicId);
        return response;
    }

    public List<ChatHistoryDto> getChatHistory(Integer clinicId, String phoneNumber, Integer page)
            throws NotFoundException, WhatsAppNotConnectedException {
        return wahaService.getChatHistory(clinicId, phoneNumber, page);
    }
}
