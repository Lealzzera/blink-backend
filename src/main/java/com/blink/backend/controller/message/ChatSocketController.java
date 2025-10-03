package com.blink.backend.controller.message;

import com.blink.backend.controller.message.dto.SendMessageRequest;
import com.blink.backend.domain.exception.NotFoundException;
import com.blink.backend.domain.exception.message.WhatsAppNotConnectedException;
import com.blink.backend.domain.service.WhatsAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatSocketController {
    private final WhatsAppService whatsAppService;

    @MessageMapping("/send-message")
    public void sendMessage(@Payload SendMessageRequest sendMessageRequest)
            throws NotFoundException, WhatsAppNotConnectedException, InterruptedException {
        whatsAppService.sendMessage(sendMessageRequest);
    }
}
