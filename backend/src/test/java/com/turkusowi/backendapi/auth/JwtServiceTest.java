package com.turkusowi.backendapi.auth;

import com.turkusowi.backendapi.role.Rola;
import com.turkusowi.backendapi.uzytkownicy.Uzytkownik;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private final JwtService jwtService = new JwtService(
            "turkusowi-backend-jwt-dev-secret-key-please-change-in-production-2026",
            60_000
    );

    @Test
    void shouldGenerateAndValidateToken() {
        Uzytkownik user = buildUser(7, "admin@schronisko.pl", "ADMIN");

        String token = jwtService.generateToken(user);
        String role = jwtService.extractClaim(token, claims -> claims.get("role", String.class));
        Integer userId = jwtService.extractClaim(token, claims -> claims.get("userId", Integer.class));

        assertThat(token).isNotBlank();
        assertThat(jwtService.extractEmail(token)).isEqualTo("admin@schronisko.pl");
        assertThat(jwtService.isTokenValid(token, user)).isTrue();
        assertThat(role).isEqualTo("ADMIN");
        assertThat(userId).isEqualTo(7);
    }

    private Uzytkownik buildUser(Integer id, String email, String roleName) {
        Uzytkownik user = new Uzytkownik();
        setField(user, "id", id);
        user.setEmail(email);
        user.setImie("Anna");
        user.setNazwisko("Nowak");

        Rola role = new Rola();
        setField(role, "id", 1);
        role.setNazwa(roleName);
        user.setRola(role);

        return user;
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
