package com.blink.backend.domain.service;

import com.blink.backend.controller.message.dto.MessageReceivedRequest;
import com.blink.backend.controller.message.dto.SendMessageRequest;
import com.blink.backend.controller.message.dto.WhatsAppStatusDto;
import com.blink.backend.domain.exception.NotFoundException;
import com.blink.backend.domain.exception.message.WhatsAppNotConnectedException;

public interface WhatsAppService {

    WhatsAppStatusDto getWhatsAppStatusByClinicId(Integer clinicId) throws NotFoundException;
    byte[] getWhatsAppQrCodeByClinic(Integer clinicId) throws NotFoundException;
    void sendMessage(SendMessageRequest sendMessageRequest) throws NotFoundException, WhatsAppNotConnectedException;
    void receiveMessage(MessageReceivedRequest message) throws NotFoundException;
}
