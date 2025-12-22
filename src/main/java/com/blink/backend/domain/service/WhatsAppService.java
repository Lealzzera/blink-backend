package com.blink.backend.domain.service;

import com.blink.backend.controller.message.dto.MessageReceivedRequest;
import com.blink.backend.controller.message.dto.SendMessageRequest;
import com.blink.backend.controller.message.dto.WhatsAppStatusDto;
import com.blink.backend.domain.exception.NotFoundException;
import com.blink.backend.domain.exception.message.WhatsAppNotConnectedException;
import com.blink.backend.persistence.entity.clinic.ClinicEntity;
import org.springframework.scheduling.annotation.Async;

public interface WhatsAppService {

    WhatsAppStatusDto getWhatsAppStatusByClinicId(Integer clinicId) throws NotFoundException;

    byte[] getWhatsAppQrCodeByClinic(Integer clinicId) throws NotFoundException;

    @Async
    void sendMessage(ClinicEntity clinic, SendMessageRequest sendMessageRequest) throws WhatsAppNotConnectedException, InterruptedException;

    void sendMessage(SendMessageRequest sendMessageRequest) throws NotFoundException, WhatsAppNotConnectedException, InterruptedException;

    void receiveMessage(MessageReceivedRequest message) throws NotFoundException;

    WhatsAppStatusDto disconnectWhatsAppNumber(Integer clinicId) throws NotFoundException;
}
