package com.blink.backend.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class FeignConfigs {
    public static class WahaClientConfig {
        @Value("${WAHA_API_KEY}")
        private String wahaApiKey;

        @Bean
        public RequestInterceptor wahaClientInterceptor() {
            return template -> template.header("Authorization", "Bearer ".concat(wahaApiKey));
        }
    }
}
