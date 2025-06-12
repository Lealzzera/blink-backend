package com.blink.backend.domain.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Log4j2
@Component
@RequiredArgsConstructor
public class WahaClient {
    @Value("${waha-url}")
    private String wahaUrl;
    private final WebClient.Builder webClientBuilder;

    public byte[] getWahaQrCode() {
        WebClient webClient =  webClientBuilder.baseUrl(wahaUrl).build();
        return webClient.get()
                .uri("/api/default/auth/qr")
                .accept(MediaType.IMAGE_PNG)
                .retrieve()
                .bodyToMono(byte[].class)
                .block();
    }
}
