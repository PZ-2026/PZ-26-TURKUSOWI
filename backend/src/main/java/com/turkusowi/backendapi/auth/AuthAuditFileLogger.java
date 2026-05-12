package com.turkusowi.backendapi.auth;

import com.turkusowi.backendapi.uzytkownicy.Uzytkownik;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class AuthAuditFileLogger {

    private static final Logger applicationLogger = LoggerFactory.getLogger(AuthAuditFileLogger.class);
    private static final DateTimeFormatter FILE_DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Path logDirectory;
    private final Clock clock;

    @Autowired
    public AuthAuditFileLogger(@Value("${app.auth.audit-log-dir:logs/auth}") String configuredDirectory) {
        this(resolveLogDirectory(configuredDirectory), Clock.systemDefaultZone());
        applicationLogger.info("Logi auth beda zapisywane w katalogu {}", this.logDirectory);
    }

    AuthAuditFileLogger(Path logDirectory, Clock clock) {
        this.logDirectory = logDirectory.toAbsolutePath().normalize();
        this.clock = clock;
    }

    public void logLoginSuccess(Uzytkownik uzytkownik) {
        writeEntry(
                "LOGIN_SUCCESS",
                "email=" + sanitize(uzytkownik.getEmail())
                        + " | userId=" + sanitize(String.valueOf(uzytkownik.getId()))
                        + " | role=" + sanitize(uzytkownik.getRola().getNazwa())
        );
    }

    public void logLoginFailure(String email, String reason) {
        writeEntry(
                "LOGIN_FAILURE",
                "email=" + sanitize(email) + " | reason=" + sanitize(reason)
        );
    }

    public void logRegisterSuccess(Uzytkownik uzytkownik) {
        writeEntry(
                "REGISTER_SUCCESS",
                "email=" + sanitize(uzytkownik.getEmail())
                        + " | userId=" + sanitize(String.valueOf(uzytkownik.getId()))
                        + " | role=" + sanitize(uzytkownik.getRola().getNazwa())
        );
    }

    public void logRegisterFailure(String email, String reason) {
        writeEntry(
                "REGISTER_FAILURE",
                "email=" + sanitize(email) + " | reason=" + sanitize(reason)
        );
    }

    public Path getLogDirectory() {
        return logDirectory;
    }

    private synchronized void writeEntry(String eventType, String details) {
        LocalDate currentDate = LocalDate.now(clock);
        Path logFile = logDirectory.resolve("auth-" + FILE_DATE_FORMATTER.format(currentDate) + ".txt");
        String line = TIMESTAMP_FORMATTER.format(LocalDateTime.now(clock))
                + " | " + sanitize(eventType)
                + " | " + details
                + System.lineSeparator();

        try {
            Files.createDirectories(logDirectory);
            Files.writeString(logFile, line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException exception) {
            applicationLogger.error("Nie udalo sie zapisac logu auth do pliku {}", logFile, exception);
        }
    }

    private static String sanitize(String value) {
        if (value == null) {
            return "-";
        }
        return value.replace('\r', ' ').replace('\n', ' ').trim();
    }

    private static Path resolveLogDirectory(String configuredDirectory) {
        Path configuredPath = Path.of(configuredDirectory);
        if (configuredPath.isAbsolute()) {
            return configuredPath;
        }

        return findBackendRoot().resolve(configuredPath);
    }

    private static Path findBackendRoot() {
        Path workingDirectory = Path.of(System.getProperty("user.dir")).toAbsolutePath().normalize();
        Path rootFromWorkingDirectory = searchForBackendRoot(workingDirectory);
        if (rootFromWorkingDirectory != null) {
            return rootFromWorkingDirectory;
        }

        File applicationDirectory = new ApplicationHome(AuthAuditFileLogger.class).getDir();
        if (applicationDirectory != null) {
            Path rootFromApplicationDirectory = searchForBackendRoot(applicationDirectory.toPath().toAbsolutePath().normalize());
            if (rootFromApplicationDirectory != null) {
                return rootFromApplicationDirectory;
            }
        }

        return workingDirectory;
    }

    private static Path searchForBackendRoot(Path startPath) {
        for (Path current = startPath; current != null; current = current.getParent()) {
            if (isBackendRoot(current)) {
                return current;
            }

            Path backendChild = current.resolve("backend");
            if (isBackendRoot(backendChild)) {
                return backendChild;
            }
        }

        return null;
    }

    private static boolean isBackendRoot(Path candidate) {
        return candidate != null
                && Files.exists(candidate.resolve("build.gradle.kts"))
                && Files.exists(candidate.resolve("src/main/resources/application.yaml"));
    }
}
