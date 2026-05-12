package com.turkusowi.backendapi.auth;

import com.turkusowi.backendapi.common.ConflictException;
import com.turkusowi.backendapi.common.NotFoundException;
import com.turkusowi.backendapi.role.Rola;
import com.turkusowi.backendapi.role.RolaService;
import com.turkusowi.backendapi.uzytkownicy.Uzytkownik;
import com.turkusowi.backendapi.uzytkownicy.UzytkownikRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UzytkownikRepository uzytkownikRepository;

    @Mock
    private RolaService rolaService;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthAuditFileLogger authAuditFileLogger;

    @InjectMocks
    private AuthService authService;

    @Test
    void shouldLoginActiveUserAndUpdateLastLogin() {
        Uzytkownik user = buildUser(1, "admin@schronisko.pl", "admin123", true, "ADMIN");
        given(uzytkownikRepository.findByEmailIgnoreCase("admin@schronisko.pl")).willReturn(Optional.of(user));
        given(uzytkownikRepository.save(user)).willReturn(user);
        given(passwordEncoder.matches("admin123", "admin123")).willThrow(new IllegalArgumentException("legacy"));
        given(passwordEncoder.encode("admin123")).willReturn("$2a$encoded-admin123");
        given(jwtService.generateToken(user)).willReturn("jwt-token");

        AuthUserResponse response = authService.login(new LoginRequest("admin@schronisko.pl", "admin123"));

        assertThat(response.email()).isEqualTo("admin@schronisko.pl");
        assertThat(response.rola()).isEqualTo("ADMIN");
        assertThat(response.accessToken()).isEqualTo("jwt-token");
        assertThat(user.getOstatnieLogowanie()).isNotNull();
        assertThat(user.getHasloHash()).isEqualTo("$2a$encoded-admin123");
        verify(uzytkownikRepository).save(user);
        verify(authAuditFileLogger).logLoginSuccess(user);
    }

    @Test
    void shouldRejectLoginForInactiveUser() {
        Uzytkownik user = buildUser(1, "admin@schronisko.pl", "admin123", false, "ADMIN");
        given(uzytkownikRepository.findByEmailIgnoreCase("admin@schronisko.pl")).willReturn(Optional.of(user));

        assertThatThrownBy(() -> authService.login(new LoginRequest("admin@schronisko.pl", "admin123")))
                .isInstanceOf(ConflictException.class)
                .hasMessage("To konto jest nieaktywne.");

        verify(authAuditFileLogger).logLoginFailure("admin@schronisko.pl", "To konto jest nieaktywne.");
    }

    @Test
    void shouldRejectLoginForInvalidPassword() {
        Uzytkownik user = buildUser(1, "admin@schronisko.pl", "admin123", true, "ADMIN");
        given(uzytkownikRepository.findByEmailIgnoreCase("admin@schronisko.pl")).willReturn(Optional.of(user));
        given(passwordEncoder.matches("wrong", "admin123")).willThrow(new IllegalArgumentException("legacy"));

        assertThatThrownBy(() -> authService.login(new LoginRequest("admin@schronisko.pl", "wrong")))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Niepoprawne dane logowania.");

        verify(authAuditFileLogger).logLoginFailure("admin@schronisko.pl", "Niepoprawne dane logowania.");
    }

    @Test
    void shouldRejectLoginWhenUserDoesNotExist() {
        given(uzytkownikRepository.findByEmailIgnoreCase("ghost@schronisko.pl")).willReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(new LoginRequest(" ghost@schronisko.pl ", "whatever")))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Nie znaleziono konta dla podanego adresu email.");

        verify(authAuditFileLogger).logLoginFailure("ghost@schronisko.pl", "Nie znaleziono konta dla podanego adresu email.");
    }

    @Test
    void shouldRegisterVolunteerUser() {
        Rola volunteerRole = buildRole(3, "WOLONTARIUSZ");
        given(uzytkownikRepository.existsByEmailIgnoreCase("nowy@schronisko.pl")).willReturn(false);
        given(rolaService.getRequiredVolunteerRole()).willReturn(volunteerRole);
        given(passwordEncoder.encode("Haslo123!")).willReturn("$2a$encoded-register");
        given(uzytkownikRepository.save(any(Uzytkownik.class))).willAnswer(invocation -> {
            Uzytkownik saved = invocation.getArgument(0);
            setField(saved, "id", 5);
            return saved;
        });
        given(jwtService.generateToken(any(Uzytkownik.class))).willReturn("register-token");

        AuthUserResponse response = authService.register(
                new RegisterRequest("  Jan  ", "  Kowalski  ", " nowy@schronisko.pl ", "Haslo123!")
        );

        ArgumentCaptor<Uzytkownik> captor = ArgumentCaptor.forClass(Uzytkownik.class);
        verify(uzytkownikRepository).save(captor.capture());

        Uzytkownik savedUser = captor.getValue();
        assertThat(savedUser.getImie()).isEqualTo("Jan");
        assertThat(savedUser.getNazwisko()).isEqualTo("Kowalski");
        assertThat(savedUser.getEmail()).isEqualTo("nowy@schronisko.pl");
        assertThat(savedUser.getHasloHash()).isEqualTo("$2a$encoded-register");
        assertThat(savedUser.getRola().getNazwa()).isEqualTo("WOLONTARIUSZ");
        assertThat(savedUser.isCzyAktywny()).isTrue();

        assertThat(response.id()).isEqualTo(5);
        assertThat(response.rola()).isEqualTo("WOLONTARIUSZ");
        assertThat(response.accessToken()).isEqualTo("register-token");
        verify(authAuditFileLogger).logRegisterSuccess(savedUser);
    }

    @Test
    void shouldRejectRegistrationWhenEmailAlreadyExists() {
        given(uzytkownikRepository.existsByEmailIgnoreCase("admin@schronisko.pl")).willReturn(true);

        assertThatThrownBy(() -> authService.register(
                new RegisterRequest("Jan", "Kowalski", "admin@schronisko.pl", "Haslo123!")
        ))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Uzytkownik z adresem email 'admin@schronisko.pl' juz istnieje.");

        verify(authAuditFileLogger).logRegisterFailure("admin@schronisko.pl", "Uzytkownik z tym adresem email juz istnieje.");
    }

    @Test
    void shouldReturnMessageWhenForgotPasswordUserExists() {
        Uzytkownik user = buildUser(7, "user@schronisko.pl", "user123", true, "WOLONTARIUSZ");
        given(uzytkownikRepository.findByEmailIgnoreCase("user@schronisko.pl")).willReturn(Optional.of(user));

        AuthMessageResponse response = authService.forgotPassword(new ForgotPasswordRequest("user@schronisko.pl"));

        assertThat(response.message()).isEqualTo("Wyslalismy instrukcje resetu hasla na podany adres email.");
    }

    @Test
    void shouldRejectForgotPasswordWhenUserDoesNotExist() {
        given(uzytkownikRepository.findByEmailIgnoreCase("ghost@schronisko.pl")).willReturn(Optional.empty());

        assertThatThrownBy(() -> authService.forgotPassword(new ForgotPasswordRequest("ghost@schronisko.pl")))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Nie znaleziono konta dla podanego adresu email.");
    }

    private Uzytkownik buildUser(Integer id, String email, String password, boolean active, String roleName) {
        Uzytkownik user = new Uzytkownik();
        setField(user, "id", id);
        user.setEmail(email);
        user.setHasloHash(password);
        user.setImie("Anna");
        user.setNazwisko("Nowak");
        user.setCzyAktywny(active);
        user.setOstatnieLogowanie(LocalDateTime.of(2026, 1, 1, 12, 0));
        user.setRola(buildRole(1, roleName));
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
