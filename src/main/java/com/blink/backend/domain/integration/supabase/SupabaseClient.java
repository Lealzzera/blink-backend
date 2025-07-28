package com.blink.backend.domain.integration.supabase;

import com.blink.backend.domain.integration.supabase.dto.SupabaseUserDetailsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Component
@FeignClient(name = "supabase-client", url = "${supabase-url}", dismiss404 = true)
public interface SupabaseClient {

    @GetMapping(value = "/auth/v1/user",
            headers = {
                    "apikey=${supabase-api-key}"
            })
    SupabaseUserDetailsResponse getUserInfo(@RequestHeader("Authorization") String authorization);

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();

        headers.add("apikey", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im50ZGdlbGNwcnZuaHRxaGh0YmdiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDU0MTQ3MTMsImV4cCI6MjA2MDk5MDcxM30.yLcMXJKPTuCqddJOkQIHZxInyBDaR55mt8GA-dZwgXI");
        return headers;
    }
}
