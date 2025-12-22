package com.blink.backend.domain.service;

import com.blink.backend.controller.message.dto.ChatHistoryDto;
import com.blink.backend.controller.message.dto.ChatOverviewDto;
import com.blink.backend.domain.exception.NotFoundException;
import com.blink.backend.domain.exception.message.WhatsAppNotConnectedException;
import com.blink.backend.persistence.entity.appointment.Patient;
import com.blink.backend.persistence.entity.clinic.ClinicEntity;
import com.blink.backend.persistence.repository.PatientRepository;
import com.blink.backend.persistence.repository.clinic.ClinicRepositoryService;
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
    private final ClinicRepositoryService clinicRepository;

    public Boolean toggleAiAnswerMode(Integer clinicId, String phoneNumber) throws NotFoundException {
        Patient patient = patientRepository.findByClinic_IdAndPhoneNumber(clinicId, phoneNumber)
                .orElseThrow(() -> new NotFoundException("Paciente"));
        patient.setAiAnswer(patient.getAiAnswer());
        patientRepository.save(patient);
        return patient.getAiAnswer();
    }

    @Deprecated
    public List<ChatOverviewDto> getChatOverView(Integer clinicId, Integer page, Integer pageSize)
            throws NotFoundException, WhatsAppNotConnectedException {
        ClinicEntity clinic = clinicRepository.findById(clinicId);
        return getChatOverView(clinic, page, pageSize);
    }

    public List<ChatOverviewDto> getChatOverView(ClinicEntity clinic, Integer page, Integer pageSize)
            throws WhatsAppNotConnectedException {
        log.info("init-get-chat-overview, clinicId={}", clinic.getId());
        List<ChatOverviewDto> response = wahaService.getChatsOverview(clinic, page, pageSize);
        log.info("end-get-chat-overview, clinicId={}", clinic.getId());
        return response;
    }

    @Deprecated
    public List<ChatHistoryDto> getChatHistory(Integer clinicId, String phoneNumber, Integer page, Integer pageSize)
            throws NotFoundException, WhatsAppNotConnectedException {
        ClinicEntity clinic = clinicRepository.findById(clinicId);
        return getChatHistory(clinic, phoneNumber, page, pageSize);
    }

    public List<ChatHistoryDto> getChatHistory(ClinicEntity clinic, String phoneNumber, Integer page, Integer pageSize)
            throws WhatsAppNotConnectedException {
        return wahaService.getChatHistory(clinic, phoneNumber, page, pageSize);
    }
}
