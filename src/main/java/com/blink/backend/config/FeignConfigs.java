package com.blink.backend.config;

import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfigs {
    @RequiredArgsConstructor
    public static class WahaClientConfig {
        @Value("${WAHA_API_KEY}")
        private String wahaApiKey;
        @Bean
        public RequestInterceptor clientAInterceptor() {
            return template -> template.header("Authorization", wahaApiKey);
        }
    }
}
