package com.turkusowi.backendapi.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    // Wstrzykujemy naszego "Urzędnika od pieczątek", którego napisaliśmy wcześniej
    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 1. Sprawdzamy czy klient w ogóle ma opaskę na ręku (nagłówek zaczynający się od "Bearer ")
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Nie ma opaski? Przepuszczamy go dalej do drzwi.
            // Szef Ochrony (SecurityConfig) i tak go wyrzuci, jeśli endpoint jest chroniony.
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Ucinamy pierwsze 7 znaków ("Bearer "), żeby zostawić sam czysty kod tokena
        jwt = authHeader.substring(7);

        try {
            // 3. Próbujemy odczytać imię z tokena.
            // Jeśli token wygasł lub ktoś go podrobił, ta funkcja automatycznie rzuci błędem!
            userEmail = jwtService.extractUsername(jwt);
        } catch (JwtException | IllegalArgumentException e) {
            // Haker albo przeterminowany bilet? Odrzucamy na wejściu i zwracamy ładny błąd JSON.
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\": \"Nieprawidłowy lub wygasły token JWT\"}");
            return;
        }

        // 4. Jeśli token jest super i jeszcze nie zalogowaliśmy tego gościa w tym zapytaniu...
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Tworzymy mu "wejściówkę" na serwer na ten ułamek sekundy.
            // Collections.emptyList() oznacza, że na razie nie wnikamy w jego role (ADMIN/USER), po prostu go wpuszczamy.
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userEmail,
                    null,
                    Collections.emptyList()
            );

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Zapisujemy go w systemie Springa jako zalogowanego
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        // 5. Wszystko gra, gość wchodzi do środka (np. do kontrolera ze zwierzętami)
        filterChain.doFilter(request, response);
    }
}