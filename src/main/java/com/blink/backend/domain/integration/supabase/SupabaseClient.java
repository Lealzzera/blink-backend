package com.blink.backend.domain.integration.supabase;

import com.blink.backend.domain.integration.supabase.dto.SupabaseUserDetailsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Component
@FeignClient(name = "supabase-client", url = "${supabase-url}", dismiss404 = true)
interface SupabaseClient {

    @GetMapping(value = "/auth/v1/user",
            headers = {
                    "apikey=${supabase-api-key}"
            })
    SupabaseUserDetailsResponse getUserInfo(@RequestHeader("Authorization") String authorization);
}
