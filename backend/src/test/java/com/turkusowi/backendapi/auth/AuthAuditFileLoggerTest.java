package com.turkusowi.backendapi.auth;

import com.turkusowi.backendapi.role.Rola;
import com.turkusowi.backendapi.uzytkownicy.Uzytkownik;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

class AuthAuditFileLoggerTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldWriteAuthEventsToDailyLogFile() throws IOException {
        Clock fixedClock = Clock.fixed(Instant.parse("2026-05-12T08:15:30Z"), ZoneId.of("Europe/Warsaw"));
        AuthAuditFileLogger authAuditFileLogger = new AuthAuditFileLogger(tempDir, fixedClock);

        authAuditFileLogger.logLoginSuccess(buildUser(12, "admin@schronisko.pl", "ADMIN"));
        authAuditFileLogger.logRegisterFailure("nowy@schronisko.pl", "Uzytkownik juz istnieje.");

        Path logFile = tempDir.resolve("auth-2026-05-12.txt");
        String fileContents = Files.readString(logFile);

        assertThat(logFile).exists();
        assertThat(fileContents).contains("2026-05-12 10:15:30");
        assertThat(fileContents).contains("LOGIN_SUCCESS");
        assertThat(fileContents).contains("REGISTER_FAILURE");
        assertThat(fileContents).contains("email=admin@schronisko.pl");
        assertThat(fileContents).contains("userId=12");
        assertThat(fileContents).contains("role=ADMIN");
        assertThat(fileContents).contains("email=nowy@schronisko.pl");
        assertThat(fileContents).contains("reason=Uzytkownik juz istnieje.");
    }

    private Uzytkownik buildUser(Integer id, String email, String roleName) {
        Uzytkownik user = new Uzytkownik();
        setField(user, "id", id);
        user.setEmail(email);
        user.setRola(buildRole(3, roleName));
        return user;
    }

    private Rola buildRole(Integer id, String roleName) {
        Rola role = new Rola();
        setField(role, "id", id);
        role.setNazwa(roleName);
        return role;
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
