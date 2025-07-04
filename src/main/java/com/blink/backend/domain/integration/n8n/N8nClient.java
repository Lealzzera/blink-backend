package com.blink.backend.domain.integration.n8n;

import com.blink.backend.domain.integration.n8n.dto.N8nMessageReceived;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(name = "n8n-client", url = "${n8n-url}", dismiss404 = true)
public interface N8nClient {

    @PostMapping("webhook/n8n/receive-message")
    ResponseEntity<String> receiveMessage(@RequestBody N8nMessageReceived message);
}
