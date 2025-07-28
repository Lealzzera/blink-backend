package com.blink.backend.domain.model.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@AllArgsConstructor
public enum Authorities implements GrantedAuthority {
    AUTHENTICATED("authenticated"),
    ;
    private String authority;
}
