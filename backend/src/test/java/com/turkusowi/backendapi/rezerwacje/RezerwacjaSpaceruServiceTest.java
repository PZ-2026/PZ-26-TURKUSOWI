package com.turkusowi.backendapi.rezerwacje;

import com.turkusowi.backendapi.common.ConflictException;
import com.turkusowi.backendapi.role.Rola;
import com.turkusowi.backendapi.uzytkownicy.Uzytkownik;
import com.turkusowi.backendapi.uzytkownicy.UzytkownikService;
import com.turkusowi.backendapi.zwierzeta.Zwierze;
import com.turkusowi.backendapi.zwierzeta.ZwierzeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RezerwacjaSpaceruServiceTest {

    @Mock
    private RezerwacjaSpaceruRepository rezerwacjaSpaceruRepository;

    @Mock
    private UzytkownikService uzytkownikService;

    @Mock
    private ZwierzeService zwierzeService;

    @InjectMocks
    private RezerwacjaSpaceruService rezerwacjaSpaceruService;

    @Test
    void shouldRejectReservationWhenVolunteerRoleIsInvalid() {
        UpsertRezerwacjaSpaceruRequest request = new UpsertRezerwacjaSpaceruRequest(
                5,
                2,
                LocalDate.now().plusDays(1),
                LocalTime.of(10, 0),
                LocalTime.of(11, 0),
                null
        );

        given(uzytkownikService.getRequiredEntity(5)).willReturn(buildUser(5, "PRACOWNIK"));

        assertThatThrownBy(() -> rezerwacjaSpaceruService.create(request))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Rezerwacje moze utworzyc tylko wolontariusz.");
    }

    @Test
    void shouldRejectOverlappingReservation() {
        UpsertRezerwacjaSpaceruRequest request = new UpsertRezerwacjaSpaceruRequest(
                3,
                2,
                LocalDate.now().plusDays(1),
                LocalTime.of(10, 0),
                LocalTime.of(11, 0),
                null
        );

        given(uzytkownikService.getRequiredEntity(3)).willReturn(buildUser(3, "WOLONTARIUSZ"));
        given(zwierzeService.getRequiredEntity(2)).willReturn(buildAnimal(2));
        given(rezerwacjaSpaceruRepository.findByZwierzeIdAndDataSpaceru(2, request.dataSpaceru()))
                .willReturn(List.of(buildExistingReservation(2, request.dataSpaceru())));

        assertThatThrownBy(() -> rezerwacjaSpaceruService.create(request))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Wybrany termin dla tego zwierzecia jest juz zajety.");
    }

    @Test
    void shouldSaveReservationWhenSlotIsFree() {
        UpsertRezerwacjaSpaceruRequest request = new UpsertRezerwacjaSpaceruRequest(
                3,
                2,
                LocalDate.now().plusDays(1),
                LocalTime.of(12, 0),
                LocalTime.of(13, 0),
                "Bez konfliktu"
        );

        given(uzytkownikService.getRequiredEntity(3)).willReturn(buildUser(3, "WOLONTARIUSZ"));
        given(zwierzeService.getRequiredEntity(2)).willReturn(buildAnimal(2));
        given(rezerwacjaSpaceruRepository.findByZwierzeIdAndDataSpaceru(2, request.dataSpaceru()))
                .willReturn(List.of());
        given(rezerwacjaSpaceruRepository.save(any(RezerwacjaSpaceru.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        rezerwacjaSpaceruService.create(request);
    }

    private Uzytkownik buildUser(Integer id, String roleName) {
        Rola rola = new Rola();
        rola.setNazwa(roleName);

        Uzytkownik uzytkownik = new Uzytkownik();
        try {
            var field = Uzytkownik.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(uzytkownik, id);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException(exception);
        }
        uzytkownik.setImie("Jan");
        uzytkownik.setNazwisko("Kowalski");
        uzytkownik.setRola(rola);
        return uzytkownik;
    }

    private Zwierze buildAnimal(Integer id) {
        Zwierze zwierze = new Zwierze();
        try {
            var field = Zwierze.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(zwierze, id);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException(exception);
        }
        zwierze.setImie("Burek");
        return zwierze;
    }

    private RezerwacjaSpaceru buildExistingReservation(Integer animalId, LocalDate date) {
        RezerwacjaSpaceru rezerwacja = new RezerwacjaSpaceru();
        try {
            var field = RezerwacjaSpaceru.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(rezerwacja, 10);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException(exception);
        }
        rezerwacja.setZwierze(buildAnimal(animalId));
        rezerwacja.setDataSpaceru(date);
        rezerwacja.setGodzinaStart(LocalTime.of(10, 30));
        rezerwacja.setGodzinaKoniec(LocalTime.of(11, 30));
        return rezerwacja;
    }
}
