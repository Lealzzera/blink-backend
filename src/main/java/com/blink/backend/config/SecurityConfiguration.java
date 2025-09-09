package com.blink.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import static com.blink.backend.domain.model.auth.Authorities.AUTHENTICATED;
import static com.blink.backend.domain.model.auth.Authorities.N8N_AUTHENTICATED;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private static final String[] SWAGGER_MATCHER = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/actuator/**",
            "/error**"
    };

    private static final String[] WHITE_LIST_URL = {
            /*"/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"*/};

    private final AuthFilter authFilter;
    private final PasswordEncoder passwordEncoder;
    private final CorsConfigurationSource corsConfigurationSource;
    //private final EndpointLoggingInterceptor endpointLoggingInterceptor;

    @Bean
    @Order(1)
    public SecurityFilterChain swaggerSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(SWAGGER_MATCHER)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/error**").permitAll()
                        .anyRequest().hasRole("SWAGGER"))
                .httpBasic(Customizer.withDefaults())
                .authenticationProvider(swaggerAuthProvider())
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .authorizeHttpRequests(req -> req
                        .requestMatchers(POST, "/api/v1/message/whats-app/receive-message").permitAll()
                        .requestMatchers(GET, "/**").hasAnyAuthority(AUTHENTICATED.getAuthority(), N8N_AUTHENTICATED.getAuthority())
                        .requestMatchers(POST, "/**").hasAnyAuthority(AUTHENTICATED.getAuthority(), N8N_AUTHENTICATED.getAuthority())
                        .requestMatchers(PUT, "/**").hasAnyAuthority(AUTHENTICATED.getAuthority(), N8N_AUTHENTICATED.getAuthority())
                        .requestMatchers(DELETE, "/**").hasAnyAuthority(AUTHENTICATED.getAuthority(), N8N_AUTHENTICATED.getAuthority())
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                /*.logout(logout -> logout
                        .logoutUrl("/api/v1/auth/logout")
                        //.addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                )*/;
        return http.build();
    }

    @Bean
    @Order(3)
    public SecurityFilterChain webSocketSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/wpp-socket/**")
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS));
        return http.build();
    }

    @Bean
    public AuthenticationProvider swaggerAuthProvider() {
        UserDetails swaggerUser = User.withUsername("blink")
                .password(passwordEncoder.encode("blink-clinic-admin"))
                .roles("SWAGGER")
                .build();

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(new InMemoryUserDetailsManager(swaggerUser));
        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

}
