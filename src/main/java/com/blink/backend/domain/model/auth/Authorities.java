package com.blink.backend.domain.model.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@AllArgsConstructor
public enum Authorities implements GrantedAuthority {
    AUTHENTICATED("authenticated"),
    N8N_AUTHENTICATED("n8nAuthenticated"),
    ;
    private String authority;
}
