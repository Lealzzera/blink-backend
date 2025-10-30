package com.blink.backend.config;

import com.blink.backend.domain.integration.supabase.SupabaseClientService;
import com.blink.backend.domain.integration.supabase.dto.SupabaseUserDetailsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.swagger.v3.core.jackson.ModelResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class BeansConfiguration {

    private final SupabaseClientService supabaseClient;

    @Bean
    @Qualifier("methodAuth")
    public UserDetailsService userDetailsService() {
        return key -> {
            try {
                return Optional.ofNullable(supabaseClient.getUserInfo("Bearer " + key))
                        .map(SupabaseUserDetailsResponse::toDomain)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            } catch (Exception e) {
                throw new UsernameNotFoundException("User not found", e);
            }
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new DaoAuthenticationProvider(userDetailsService());
    }

    @Bean
    public ModelResolver modelResolver(ObjectMapper objectMapper) {
        return new ModelResolver(objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "https://localhost:3000",
                "ws://localhost:3000",
                "https://be.blinkdentalmarketing.com.br",
                "https://blink-fe-dev:3000",
                "http://blink-fe-dev:3000",
                "https://fe.blinkdentalmarketing.com.br",
                "http://fe.blinkdentalmarketing.com.br"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
