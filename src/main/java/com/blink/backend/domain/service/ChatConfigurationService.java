package com.blink.backend.domain.service;

import com.blink.backend.domain.exception.NotFoundException;
import com.blink.backend.persistence.entity.message.Chat;
import com.blink.backend.persistence.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatConfigurationService {
    private final ChatRepository chatRepository;

    public void toggleAiAnswerMode(Integer clinicId, String phoneNumber) throws NotFoundException {
        Chat chat = chatRepository.findByClinicIdAndPatientPhoneNumber(clinicId, phoneNumber)
                .orElseThrow(() -> new NotFoundException("Configuração de chat"));
        chat.setAiAnswer(!chat.getAiAnswer());
        chatRepository.save(chat);
    }
}
