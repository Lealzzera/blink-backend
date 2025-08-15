package com.blink.backend.domain.service;

import com.blink.backend.controller.message.dto.ChatHistoryDto;
import com.blink.backend.controller.message.dto.ChatOverviewDto;
import com.blink.backend.domain.exception.NotFoundException;
import com.blink.backend.domain.exception.message.WhatsAppNotConnectedException;
import com.blink.backend.persistence.entity.message.Chat;
import com.blink.backend.persistence.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatConfigurationService {
    private final ChatRepository chatRepository;
    private final WahaService wahaService;

    public void toggleAiAnswerMode(Integer clinicId, String phoneNumber) throws NotFoundException {
        Chat chat = chatRepository.findByClinicIdAndPatientPhoneNumber(clinicId, phoneNumber)
                .orElseThrow(() -> new NotFoundException("Configuração de chat"));
        chat.setAiAnswer(!chat.getAiAnswer());
        chatRepository.save(chat);
    }

    public List<ChatOverviewDto> getChatOverView(Integer clinicId) throws NotFoundException, WhatsAppNotConnectedException {
        return wahaService.getChatsOverview(clinicId);
    }

    public List<ChatHistoryDto> getChatHistory(Integer clinicId, String phoneNumber) throws NotFoundException, WhatsAppNotConnectedException {
        return wahaService.getChatHistory(clinicId, phoneNumber);
    }
}
