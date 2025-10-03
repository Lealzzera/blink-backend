package com.blink.backend.domain.integration.waha;

import com.blink.backend.config.FeignConfigs;
import com.blink.backend.domain.integration.waha.dto.CreateWahaSessionRequest;
import com.blink.backend.domain.integration.waha.dto.SendWahaMessageRequest;
import com.blink.backend.domain.integration.waha.dto.WahaChatHistory;
import com.blink.backend.domain.integration.waha.dto.WahaChatOverviewDto;
import com.blink.backend.domain.integration.waha.dto.WahaPresenceDto;
import com.blink.backend.domain.integration.waha.dto.WahaSessionStatusResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
@FeignClient(name = "waha-client", url = "${waha-url}", dismiss404 = true, configuration = FeignConfigs.WahaClientConfig.class)
public interface FeignWahaClient {

    @GetMapping(value = "api/{sessionName}/auth/qr", consumes = MediaType.IMAGE_PNG_VALUE)
    byte[] getWahaQrCode(@PathVariable String sessionName);

    @GetMapping(value = "api/sessions/{session}")
    ResponseEntity<WahaSessionStatusResponse> getWahaSessionStatus(@PathVariable String session);

    @PostMapping("api/sessions")
    WahaSessionStatusResponse createWahaSession(CreateWahaSessionRequest createWahaSessionRequest);

    @DeleteMapping("api/sessions/{sessionName}")
    void deleteWahaSession(@PathVariable String sessionName);

    @PostMapping(value = "api/sendText",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            headers = {"X-Api-Key=${waha-api-key}"})
    void sendMessage(SendWahaMessageRequest sendWahaMessageRequest);

    @GetMapping("api/{session}/chats/{chatId}/messages")
    ResponseEntity<List<WahaChatHistory>> getMessages(
            @PathVariable String session,
            @PathVariable String chatId,
            @RequestParam Integer limit,
            @RequestParam Integer offset);

    @GetMapping("/api/{session}/chats/overview")
    ResponseEntity<List<WahaChatOverviewDto>> getOverview(
            @PathVariable String session,
            @RequestParam Integer limit,
            @RequestParam Integer offset);

    @PostMapping("/api/sendSeen")
    void sendSeen(WahaPresenceDto wahaPresenceDto);

    @PostMapping("/api/startTyping")
    void startTyping(WahaPresenceDto wahaPresenceDto);

    @PostMapping("/api/stopTyping")
    void stopTyping(WahaPresenceDto wahaPresenceDto);
}
