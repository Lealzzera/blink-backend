package com.blink.backend.domain.integration.supabase;

import com.blink.backend.domain.exception.BadRequestException;
import com.blink.backend.domain.exception.InvalidTokenException;
import com.blink.backend.domain.integration.supabase.dto.SupabaseUserDetailsResponse;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SupabaseClientService {
    private final SupabaseClient supabaseClient;

    public SupabaseUserDetailsResponse getUserInfo(String authorization) throws InvalidTokenException {
        try {
            SupabaseUserDetailsResponse response = supabaseClient.getUserInfo(authorization);
            return response;
        }catch (FeignException.FeignClientException e){
            if(e.status() == 403){
                throw new InvalidTokenException();
            }
            throw e;
        }

    }
}
