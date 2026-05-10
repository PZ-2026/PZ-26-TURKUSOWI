package com.turkusowi.backendapi.common;

import com.turkusowi.backendapi.auth.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private static final String ADMIN = "ADMIN";
    private static final String PRACOWNIK = "PRACOWNIK";
    private static final String WOLONTARIUSZ = "WOLONTARIUSZ";

    private static final String[] STAFF_ROLES = {ADMIN, PRACOWNIK};
    private static final String[] APP_USER_ROLES = {ADMIN, PRACOWNIK, WOLONTARIUSZ};

    private static final String[] ZWIERZETA_ENDPOINTS = {"/api/zwierzeta", "/api/zwierzeta/**"};
    private static final String[] RASY_ENDPOINTS = {"/api/rasy", "/api/rasy/**"};
    private static final String[] REZERWACJE_ENDPOINTS = {"/api/rezerwacje-spacerow", "/api/rezerwacje-spacerow/**"};
    private static final String[] HISTORIA_ENDPOINTS = {"/api/historia-medyczna", "/api/historia-medyczna/**"};
    private static final String[] RAPORTY_ENDPOINTS = {"/api/raporty-operacyjne", "/api/raporty-operacyjne/**"};
    private static final String[] UZYTKOWNICY_ENDPOINTS = {"/api/uzytkownicy", "/api/uzytkownicy/**"};
    private static final String[] ROLE_ENDPOINTS = {"/api/role", "/api/role/**"};

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/zwierzeta/publiczne").permitAll()
                        .requestMatchers(HttpMethod.GET, RASY_ENDPOINTS).permitAll()
                        .requestMatchers(UZYTKOWNICY_ENDPOINTS).hasRole(ADMIN)
                        .requestMatchers(ROLE_ENDPOINTS).hasRole(ADMIN)
                        .requestMatchers(HttpMethod.GET, ZWIERZETA_ENDPOINTS).hasAnyRole(APP_USER_ROLES)
                        .requestMatchers(ZWIERZETA_ENDPOINTS).hasAnyRole(STAFF_ROLES)
                        .requestMatchers(RASY_ENDPOINTS).hasAnyRole(STAFF_ROLES)
                        .requestMatchers(REZERWACJE_ENDPOINTS).hasAnyRole(APP_USER_ROLES)
                        .requestMatchers(HISTORIA_ENDPOINTS).hasAnyRole(STAFF_ROLES)
                        .requestMatchers(RAPORTY_ENDPOINTS).hasAnyRole(STAFF_ROLES)
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
