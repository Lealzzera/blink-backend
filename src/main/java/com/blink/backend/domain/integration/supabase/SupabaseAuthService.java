package com.blink.backend.domain.integration.supabase;

import com.blink.backend.domain.exception.InvalidTokenException;
import com.blink.backend.domain.model.auth.AuthenticatedUser;
import com.blink.backend.persistence.entity.auth.UserEntity;
import com.blink.backend.persistence.repository.UserEntityRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SupabaseAuthService {
    private final SupabaseClient supabaseClient;
    private final UserEntityRepository userEntityRepository;

    public AuthenticatedUser getAuthenticatedUser(String authorization) throws InvalidTokenException {
        try {
            final String userId = supabaseClient
                    .getUserInfoByAuthToken(authorization)
                    .at("/id")
                    .asText();
            return userEntityRepository.findByUserId(UUID.fromString(userId))
                    .map(UserEntity::toAuthUser)
                    .orElseThrow(() ->
                            new BadCredentialsException("User not found or invalid token"));
        } catch (FeignException.FeignClientException e) {
            if (e.status() == 403) {
                throw new InvalidTokenException();
            }
            throw e;
        }

    }
}
