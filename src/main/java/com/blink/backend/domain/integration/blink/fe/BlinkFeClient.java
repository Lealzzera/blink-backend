package com.blink.backend.domain.integration.blink.fe;

import com.blink.backend.domain.integration.blink.fe.dto.ReceivedMessageBlinkFeRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient(name = "blink-fe-client", url = "${blink-fe-url}", dismiss404 = true)
public interface BlinkFeClient {
    @PostMapping("/api/v1/message/whats-app/receive-message")
    ResponseEntity<String> sendReceivedMessageToFrontEnd(ReceivedMessageBlinkFeRequest request);
}
