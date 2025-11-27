package com.blink.backend.domain.model.auth;

import com.blink.backend.persistence.entity.clinic.Clinic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class AuthenticatedUser implements UserDetails {
    private final String username;
    private final List<Authorities> authorities;
    private final Clinic clinic;
    private static AuthenticatedUser n8nUser;

    public String getPassword() {
        return null;
    }

    public static AuthenticatedUser getN8nAuthenticatedUser() {
        if (n8nUser == null) {
            n8nUser = new AuthenticatedUser("n8n", List.of(Authorities.N8N_AUTHENTICATED), null);
        }
        return n8nUser;
    }
}
