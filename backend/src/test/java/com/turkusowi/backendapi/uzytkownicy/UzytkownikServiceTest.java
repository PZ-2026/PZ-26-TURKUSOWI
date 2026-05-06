package com.turkusowi.backendapi.uzytkownicy;

import com.turkusowi.backendapi.common.ConflictException;
import com.turkusowi.backendapi.common.NotFoundException;
import com.turkusowi.backendapi.role.Rola;
import com.turkusowi.backendapi.role.RolaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UzytkownikServiceTest {

    @Mock
    private UzytkownikRepository uzytkownikRepository;

    @Mock
    private RolaService rolaService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UzytkownikService uzytkownikService;

    @Test
    void shouldCreateUserWithTrimmedFieldsAndRole() {
        Rola role = buildRole("PRACOWNIK");
        CreateUzytkownikRequest request = new CreateUzytkownikRequest(
                "  jan.kowalski@schronisko.pl  ",
                "  hash_123  ",
                "  Jan  ",
                "  Kowalski  ",
                2,
                true
        );

        given(uzytkownikRepository.findAll()).willReturn(List.of());
        given(rolaService.getRequiredEntity(2)).willReturn(role);
        given(passwordEncoder.encode("hash_123")).willReturn("$2a$encoded-hash_123");
        given(uzytkownikRepository.save(any(Uzytkownik.class))).willAnswer(invocation -> {
            Uzytkownik saved = invocation.getArgument(0);
            setField(saved, "id", 9);
            return saved;
        });

        UzytkownikResponse response = uzytkownikService.create(request);

        ArgumentCaptor<Uzytkownik> captor = ArgumentCaptor.forClass(Uzytkownik.class);
        verify(uzytkownikRepository).save(captor.capture());

        Uzytkownik savedUser = captor.getValue();
        assertThat(savedUser.getEmail()).isEqualTo("jan.kowalski@schronisko.pl");
        assertThat(savedUser.getHasloHash()).isEqualTo("$2a$encoded-hash_123");
        assertThat(savedUser.getImie()).isEqualTo("Jan");
        assertThat(savedUser.getNazwisko()).isEqualTo("Kowalski");
        assertThat(savedUser.getRola().getId()).isEqualTo(2);
        assertThat(savedUser.isCzyAktywny()).isTrue();

        assertThat(response.id()).isEqualTo(9);
        assertThat(response.rolaNazwa()).isEqualTo("PRACOWNIK");
    }

    @Test
    void shouldRejectCreateWhenEmailAlreadyExistsIgnoringCase() {
        Uzytkownik existingUser = buildUser(1, "JAN.KOWALSKI@SCHRONISKO.PL", "PRACOWNIK", true);
        CreateUzytkownikRequest request = new CreateUzytkownikRequest(
                "jan.kowalski@schronisko.pl",
                "hash_123",
                "Jan",
                "Kowalski",
                2,
                true
        );

        given(uzytkownikRepository.findAll()).willReturn(List.of(existingUser));

        assertThatThrownBy(() -> uzytkownikService.create(request))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Uzytkownik z adresem email 'jan.kowalski@schronisko.pl' juz istnieje.");
    }

    @Test
    void shouldFilterUsersByRoleAndStatus() {
        Uzytkownik volunteer = buildUser(1, "wolontariusz@schronisko.pl", "WOLONTARIUSZ", true);
        Uzytkownik employee = buildUser(2, "pracownik@schronisko.pl", "PRACOWNIK", true);
        Uzytkownik inactiveVolunteer = buildUser(3, "nieaktywny@schronisko.pl", "WOLONTARIUSZ", false);

        given(uzytkownikRepository.findAll(any(Sort.class)))
                .willReturn(List.of(employee, inactiveVolunteer, volunteer));

        List<UzytkownikResponse> result = uzytkownikService.findAll(3, true);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().email()).isEqualTo("wolontariusz@schronisko.pl");
        assertThat(result.getFirst().rolaNazwa()).isEqualTo("WOLONTARIUSZ");
        assertThat(result.getFirst().czyAktywny()).isTrue();
    }

    @Test
    void shouldUpdateUserStatus() {
        Uzytkownik user = buildUser(5, "user@schronisko.pl", "WOLONTARIUSZ", true);
        given(uzytkownikRepository.findById(5)).willReturn(Optional.of(user));
        given(uzytkownikRepository.save(user)).willReturn(user);

        UzytkownikResponse response = uzytkownikService.updateStatus(5, new UpdateStatusUzytkownikaRequest(false));

        assertThat(user.isCzyAktywny()).isFalse();
        assertThat(response.czyAktywny()).isFalse();
    }

    @Test
    void shouldThrowWhenUserDoesNotExist() {
        given(uzytkownikRepository.findById(99)).willReturn(Optional.empty());

        assertThatThrownBy(() -> uzytkownikService.getRequiredEntity(99))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Nie znaleziono uzytkownika o id=99");
    }

    private Uzytkownik buildUser(Integer id, String email, String roleName, boolean active) {
        Uzytkownik user = new Uzytkownik();
        setField(user, "id", id);
        user.setEmail(email);
        user.setHasloHash("hash");
        user.setImie("Jan");
        user.setNazwisko("Kowalski");
        user.setCzyAktywny(active);
        user.setRola(buildRole(roleName));
        return user;
    }

    private Rola buildRole(String roleName) {
        Rola role = new Rola();
        Integer roleId = switch (roleName) {
            case "ADMIN" -> 1;
            case "PRACOWNIK" -> 2;
            case "WOLONTARIUSZ" -> 3;
            case "SEKRETARZ" -> 4;
            default -> 99;
        };
        setField(role, "id", roleId);
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
