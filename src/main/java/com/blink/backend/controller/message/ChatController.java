package com.blink.backend.controller.message;

import com.blink.backend.domain.exception.NotFoundException;
import com.blink.backend.domain.service.ChatConfigurationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Deprecated(forRemoval = true)
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/chat")
@Tag(name = "Old-chat", description = "Apis desatualizadas de chat")
public class ChatController {
    private final ChatConfigurationService chatConfigurationService;

    @PutMapping("{clinicId}/ai-answer/{phoneNumber}")
    public ResponseEntity<Boolean> toggleChatAiAnswer(
            @PathVariable Integer clinicId,
            @PathVariable String phoneNumber)
            throws NotFoundException {
        log.info("[V1] toggleChatAiAnswer called - clinicId: {}, phoneNumber: {}", clinicId, phoneNumber);
        return ResponseEntity.ok(chatConfigurationService.toggleAiAnswerMode(clinicId, phoneNumber));
    }

}
