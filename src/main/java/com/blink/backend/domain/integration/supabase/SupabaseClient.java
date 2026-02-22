package com.blink.backend.domain.integration.supabase;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Component
@FeignClient(name = "supabase-client", url = "${supabase-url}", dismiss404 = true)
interface SupabaseClient {

    @GetMapping(value = "/auth/v1/user",
            headers = {"apikey=${supabase-api-key}"})
    JsonNode getUserInfoByAuthToken(@RequestHeader("Authorization") String authorization);
}
