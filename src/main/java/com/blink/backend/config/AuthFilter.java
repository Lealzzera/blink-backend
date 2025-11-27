package com.blink.backend.config;

import com.blink.backend.domain.exception.InvalidTokenException;
import com.blink.backend.domain.integration.supabase.SupabaseAuthService;
import com.blink.backend.domain.model.auth.AuthenticatedUser;
import com.blink.backend.domain.model.auth.Authorities;
import com.blink.backend.persistence.entity.clinic.Clinic;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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

@Log4j2
@Component
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private final SupabaseAuthService supabaseAuthService;
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
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            log.debug("User is authenticated");
            return;
        }
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

        final AuthenticatedUser authenticatedUser = AuthenticatedUser.getN8nAuthenticatedUser();
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                authenticatedUser,
                null,
                authenticatedUser.getAuthorities()
        );

        log.debug("X-api-key authentication valid");
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
        return true;
    }

    private void doSupabaseAuthentication(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException, InvalidTokenException {

        String authToken = extractAuthToken(request);

        final AuthenticatedUser authenticatedUser = supabaseAuthService.getAuthenticatedUser(authToken);

        UsernamePasswordAuthenticationToken authContext = new UsernamePasswordAuthenticationToken(
                authenticatedUser,
                null,
                authenticatedUser.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authContext);

        log.info("Supabase authorization authentication valid");
        filterChain.doFilter(request, response);
    }

    private String extractAuthToken(HttpServletRequest request) {
        log.info("Trying supabase authentication");

        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            log.debug("Authorization token found in Authorization header");
            return authHeader;
        }

        final String authParam = request.getParameter("token");
        if (authParam != null && authParam.startsWith("Bearer ")) {
            log.debug("Authorization token found in query parameter.");
            return authParam;
        }

        log.debug("Supabase authorization header or query parameter not found or invalid format");
        throw new BadCredentialsException("Authorization header or query parameter not found or invalid format");
    }
}
