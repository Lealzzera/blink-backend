package com.blink.backend.config;

import com.blink.backend.domain.integration.supabase.SupabaseClient;
import com.blink.backend.domain.integration.supabase.dto.SupabaseUserDetailsResponse;
import com.blink.backend.domain.model.auth.Authorities;
import com.blink.backend.domain.model.auth.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Log4j2
@Component
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private final SupabaseClient supabaseClient;
    @Value("${n8n-auth-api-key}")
    private final String n8nApiKeyValue;
    @Value("${n8n-username}")
    private final String n8nUsername;
    @Value("${n8n-password}")
    private final String n8nPassword;
    @Value("${WAHA_API_KEY}")
    private final String wahaApiKey;
    @Qualifier("handlerExceptionResolver")
    private final HandlerExceptionResolver resolver;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        log.debug("AuthFilter is processing request: {}", request.getRequestURI());
        try {
            if (doApiKeyAuthentication(request, response, filterChain))
                return;
            doSupabaseAuthentication(request, response, filterChain);
        } catch (Exception e) {
            resolver.resolveException(request, response, null, e);
        }

    }

    private boolean doApiKeyAuthentication(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String apiKeyHeader = request.getHeader("X-Api-Key");
        if (apiKeyHeader == null || (!apiKeyHeader.equals(n8nApiKeyValue) && !apiKeyHeader.equals(wahaApiKey))) {
            log.debug("X-api-key header invalid. Trying supabase authentication");
            return false;
        }
        log.debug("X-api-key header valid");
        final User user = new User(n8nUsername, n8nPassword, List.of(Authorities.N8N_AUTHENTICATED));
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities()
        );
        if (user.getUsername() != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            log.debug("X-api-key authentication valid");
            authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        filterChain.doFilter(request, response);
        return true;
    }

    private void doSupabaseAuthentication(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        log.info("Trying supabase authentication");
        String authHeader = request.getHeader("Authorization");

        // If header is not present, try to get it from query parameter (for WebSocket)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            final String authParam = request.getParameter("token");
            if (authParam != null && authParam.startsWith("Bearer ")) {
                authHeader = authParam;
                log.debug("Authorization token found in query parameter.");
            }
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.debug("Supabase authorization header or query parameter not found or invalid format");
            throw new BadCredentialsException("Authorization header or query parameter not found or invalid format");
        }

        final User user = Optional.ofNullable(supabaseClient.getUserInfo(authHeader))
                .map(SupabaseUserDetailsResponse::toDomain)
                .orElseThrow(() -> new BadCredentialsException("User not found or invalid token"));

        if (user.getUsername() != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    user.getAuthorities()
            );
            authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        log.info("Supabase authorization authentication valid");
        filterChain.doFilter(request, response);
    }
}
