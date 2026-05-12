package com.turkusowi.backendapi.auth;

import com.turkusowi.backendapi.common.ConflictException;
import com.turkusowi.backendapi.common.NotFoundException;
import com.turkusowi.backendapi.role.Rola;
import com.turkusowi.backendapi.role.RolaService;
import com.turkusowi.backendapi.uzytkownicy.Uzytkownik;
import com.turkusowi.backendapi.uzytkownicy.UzytkownikRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UzytkownikRepository uzytkownikRepository;
    private final RolaService rolaService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthAuditFileLogger authAuditFileLogger;

    public AuthService(
            UzytkownikRepository uzytkownikRepository,
            RolaService rolaService,
            JwtService jwtService,
            PasswordEncoder passwordEncoder,
            AuthAuditFileLogger authAuditFileLogger
    ) {
        this.uzytkownikRepository = uzytkownikRepository;
        this.rolaService = rolaService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authAuditFileLogger = authAuditFileLogger;
    }

    public AuthUserResponse login(LoginRequest request) {
        String normalizedEmail = request.email().trim();
        Uzytkownik uzytkownik = uzytkownikRepository.findByEmailIgnoreCase(normalizedEmail)
                .orElseThrow(() -> {
                    authAuditFileLogger.logLoginFailure(normalizedEmail, "Nie znaleziono konta dla podanego adresu email.");
                    return new NotFoundException("Nie znaleziono konta dla podanego adresu email.");
                });

        if (!uzytkownik.isCzyAktywny()) {
            authAuditFileLogger.logLoginFailure(normalizedEmail, "To konto jest nieaktywne.");
            throw new ConflictException("To konto jest nieaktywne.");
        }

        boolean passwordMatches = matchesPassword(request.password(), uzytkownik.getHasloHash());
        if (!passwordMatches) {
            authAuditFileLogger.logLoginFailure(normalizedEmail, "Niepoprawne dane logowania.");
            throw new ConflictException("Niepoprawne dane logowania.");
        }

        if (isLegacyPlainTextPassword(request.password(), uzytkownik.getHasloHash())) {
            uzytkownik.setHasloHash(passwordEncoder.encode(request.password()));
        }

        uzytkownik.setOstatnieLogowanie(LocalDateTime.now());
        Uzytkownik savedUser = uzytkownikRepository.save(uzytkownik);
        AuthUserResponse response = mapToAuthUser(savedUser, jwtService.generateToken(savedUser));
        authAuditFileLogger.logLoginSuccess(savedUser);
        return response;
    }

    public AuthUserResponse register(RegisterRequest request) {
        String normalizedEmail = request.email().trim();
        if (uzytkownikRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            authAuditFileLogger.logRegisterFailure(normalizedEmail, "Uzytkownik z tym adresem email juz istnieje.");
            throw new ConflictException("Uzytkownik z adresem email '" + normalizedEmail + "' juz istnieje.");
        }

        Rola volunteerRole = rolaService.getRequiredVolunteerRole();

        Uzytkownik uzytkownik = new Uzytkownik();
        uzytkownik.setImie(request.imie().trim());
        uzytkownik.setNazwisko(request.nazwisko().trim());
        uzytkownik.setEmail(normalizedEmail);
        uzytkownik.setHasloHash(passwordEncoder.encode(request.password()));
        uzytkownik.setRola(volunteerRole);
        uzytkownik.setCzyAktywny(true);

        Uzytkownik savedUser = uzytkownikRepository.save(uzytkownik);
        AuthUserResponse response = mapToAuthUser(savedUser, jwtService.generateToken(savedUser));
        authAuditFileLogger.logRegisterSuccess(savedUser);
        return response;
    }

    public AuthMessageResponse forgotPassword(ForgotPasswordRequest request) {
        uzytkownikRepository.findByEmailIgnoreCase(request.email().trim())
                .orElseThrow(() -> new NotFoundException("Nie znaleziono konta dla podanego adresu email."));

        return new AuthMessageResponse("Wyslalismy instrukcje resetu hasla na podany adres email.");
    }

    private boolean matchesPassword(String rawPassword, String storedPasswordHash) {
        try {
            return passwordEncoder.matches(rawPassword, storedPasswordHash);
        } catch (IllegalArgumentException ignored) {
            return rawPassword.equals(storedPasswordHash);
        }
    }

    private boolean isLegacyPlainTextPassword(String rawPassword, String storedPasswordHash) {
        return rawPassword.equals(storedPasswordHash);
    }

    private AuthUserResponse mapToAuthUser(Uzytkownik uzytkownik, String accessToken) {
        return new AuthUserResponse(
                uzytkownik.getId(),
                uzytkownik.getEmail(),
                uzytkownik.getImie(),
                uzytkownik.getNazwisko(),
                uzytkownik.getRola().getNazwa(),
                uzytkownik.isCzyAktywny(),
                accessToken,
                "Bearer"
        );
    }
}
