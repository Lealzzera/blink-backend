package com.blink.backend.controller.message;

import com.blink.backend.controller.message.dto.ChatHistoryDto;
import com.blink.backend.controller.message.dto.ChatOverviewDto;
import com.blink.backend.domain.exception.NotFoundException;
import com.blink.backend.domain.exception.message.WhatsAppNotConnectedException;
import com.blink.backend.domain.service.ChatConfigurationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping("{clinicId}/overview")
    @Operation(summary = "Retorna a lista de conversas mais recentes do whats-app")
    public ResponseEntity<List<ChatOverviewDto>> getChatsOverview(
            @PathVariable Integer clinicId,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer pageSize)
            throws NotFoundException, WhatsAppNotConnectedException {
        log.info("[V1] getChatsOverview called - clinicId: {}, page: {}, pageSize: {}", clinicId, page, pageSize);
        return ResponseEntity.ok(chatConfigurationService.getChatOverView(clinicId, page, pageSize));
    }

    @GetMapping("{clinicId}/overview/{phoneNumber}")
    public ResponseEntity<List<ChatHistoryDto>> getChatHistory(
            @PathVariable Integer clinicId,
            @PathVariable String phoneNumber,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer pageSize)
            throws NotFoundException, WhatsAppNotConnectedException {
        log.info("[V1] getChatHistory called - clinicId: {}, phoneNumber: {}, page: {}, pageSize: {}", clinicId, phoneNumber, page, pageSize);
        return ResponseEntity.ok(chatConfigurationService.getChatHistory(clinicId, phoneNumber, page, pageSize));
    }

}
