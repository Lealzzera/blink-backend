package com.blink.backend.domain.integration.supabase.dto;

import com.blink.backend.domain.model.auth.Authorities;
import com.blink.backend.domain.model.auth.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupabaseUserDetailsResponse {
    private String email;
    private String role;
    private SupabaseUser user;

    public User toDomain() {
        return  User.builder()
                .username(email)
                .password("")
                .authorities(List.of(Authorities.valueOf(role.toUpperCase())))
                .build();
    }
}
