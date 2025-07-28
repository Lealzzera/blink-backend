package com.blink.backend.domain.model.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class User implements UserDetails {
    private final String username;
    private final String password;
    private final List<Authorities> authorities;
}
