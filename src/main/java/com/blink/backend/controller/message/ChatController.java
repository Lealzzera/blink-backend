package com.blink.backend.controller.message;

import com.blink.backend.controller.message.dto.ChatHistoryDto;
import com.blink.backend.controller.message.dto.ChatOverviewDto;
import com.blink.backend.domain.exception.NotFoundException;
import com.blink.backend.domain.service.ChatConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping("{clinicId}/overview")
    public ResponseEntity<List<ChatOverviewDto>> getChatsOverview(
            @PathVariable Integer clinicId)
            throws NotFoundException {
        return ResponseEntity.ok(chatConfigurationService.getChatOverView(clinicId));
    }

    @GetMapping("{clinicId}/overview/{phoneNumber}")
    public ResponseEntity<List<ChatHistoryDto>> getChatHistory(
            @PathVariable Integer clinicId,
            @PathVariable String phoneNumber)
            throws NotFoundException {
        return ResponseEntity.ok(chatConfigurationService.getChatHistory(clinicId, phoneNumber));
    }

}
