package com.blink.backend.controller.message;

import com.blink.backend.domain.exception.NotFoundException;
import com.blink.backend.domain.service.ChatConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/chat")
public class ChatController {
    private final ChatConfigurationService chatConfigurationService;

    @PutMapping("{clinicId}/ai-answer/{phoneNumber}")
    public ResponseEntity<Void> toggleChatAiAnswer(
            @PathVariable Integer clinicId,
            @PathVariable String phoneNumber)
            throws NotFoundException {
        chatConfigurationService.toggleAiAnswerMode(clinicId, phoneNumber);
        return ResponseEntity.ok().build();
    }
}
