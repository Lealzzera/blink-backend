package com.blink.backend.controller.message;

import com.blink.backend.controller.message.dto.ChatHistoryDto;
import com.blink.backend.controller.message.dto.ChatOverviewDto;
import com.blink.backend.controller.message.dto.MessageReceivedRequest;
import com.blink.backend.controller.message.dto.SendMessageRequest;
import com.blink.backend.controller.message.dto.WhatsAppStatusDto;
import com.blink.backend.domain.exception.NotFoundException;
import com.blink.backend.domain.exception.message.WhatsAppNotConnectedException;
import com.blink.backend.domain.model.auth.AuthenticatedUser;
import com.blink.backend.domain.service.ChatConfigurationService;
import com.blink.backend.domain.service.WhatsAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Deprecated(forRemoval = true)
@Slf4j
@Tag(name = "Whats app chat", description = "Novas apis de chat do whats-app")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/chat/whats-app")
public class MessageController {

    private final WhatsAppService whatsAppService;
    private final ChatConfigurationService chatConfigurationService;

    @GetMapping("qr-code")
    public ResponseEntity<byte[]> getWahaQrCode(@AuthenticationPrincipal AuthenticatedUser user) throws NotFoundException {
        log.info("[V1] getWahaQrCode called - clinicId: {}", user.getClinic().getId());
        return ResponseEntity.ok(whatsAppService.getWhatsAppQrCodeByClinic(user.getClinic().getId()));
    }

    @GetMapping("status")
    public ResponseEntity<WhatsAppStatusDto> getWhatsAppStatus(@AuthenticationPrincipal AuthenticatedUser user) throws NotFoundException {
        log.info("[V1] getWhatsAppStatus called - clinicId: {}", user.getClinic().getId());
        return ResponseEntity.ok(whatsAppService.getWhatsAppStatusByClinicId(user.getClinic().getId()));
    }

    @DeleteMapping("disconnect")
    public ResponseEntity<WhatsAppStatusDto> disconnect(@AuthenticationPrincipal AuthenticatedUser user) throws NotFoundException {
        log.info("[V1] disconnect called - clinicId: {}", user.getClinic().getId());
        return ResponseEntity.ok(whatsAppService.disconnectWhatsAppNumber(user.getClinic().getId()));
    }

    @PostMapping("send-message")
    public ResponseEntity<Void> sendMessage(
            @AuthenticationPrincipal AuthenticatedUser user,
            @RequestBody SendMessageRequest sendMessageRequest)
            throws WhatsAppNotConnectedException, InterruptedException {
        log.info("[V1] sendMessage called - clinicId: {}, phoneNumber: {}", user.getClinic().getId(), sendMessageRequest.getPhoneNumber());
        whatsAppService.sendMessage(user.getClinic(), sendMessageRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("receive-message")
    public ResponseEntity<Void> receiveMessage(
            @RequestBody MessageReceivedRequest message)
            throws NotFoundException {
        log.info("[V1] receiveMessage called - session: {}", message.getSession());
        whatsAppService.receiveMessage(message);
        return ResponseEntity.ok().build();
    }

    @PutMapping("ai-answer/{phoneNumber}")
    public ResponseEntity<Boolean> toggleChatAiAnswer(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable String phoneNumber)
            throws NotFoundException {
        log.info("[V1] toggleChatAiAnswer called - clinicId: {}, phoneNumber: {}", user.getClinic().getId(), phoneNumber);
        return ResponseEntity.ok(chatConfigurationService.toggleAiAnswerMode(user.getClinic().getId(), phoneNumber));
    }

    @GetMapping("overview")
    @Operation(summary = "Retorna a lista de conversas mais recentes do whats-app")
    public ResponseEntity<List<ChatOverviewDto>> getChatsOverview(
            @AuthenticationPrincipal AuthenticatedUser user,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer pageSize)
            throws WhatsAppNotConnectedException {
        log.info("[V1] getChatsOverview called - clinicId: {}, page: {}, pageSize: {}", user.getClinic().getId(), page, pageSize);
        return ResponseEntity.ok(chatConfigurationService.getChatOverView(user.getClinic(), page, pageSize));
    }

    @GetMapping("overview/{phoneNumber}")
    public ResponseEntity<List<ChatHistoryDto>> getChatHistory(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable String phoneNumber,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer pageSize)
            throws WhatsAppNotConnectedException {
        log.info("[V1] getChatHistory called - clinicId: {}, phoneNumber: {}, page: {}, pageSize: {}", user.getClinic().getId(), phoneNumber, page, pageSize);
        return ResponseEntity.ok(chatConfigurationService.getChatHistory(user.getClinic(), phoneNumber, page, pageSize));
    }
}
