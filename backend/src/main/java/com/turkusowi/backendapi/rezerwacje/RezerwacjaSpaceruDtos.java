package com.turkusowi.backendapi.rezerwacje;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

record UpsertRezerwacjaSpaceruRequest(
        @NotNull(message = "Id wolontariusza jest wymagane.")
        Integer wolontariuszId,

        @NotNull(message = "Id zwierzecia jest wymagane.")
        Integer zwierzeId,

        @NotNull(message = "Data spaceru jest wymagana.")
        @FutureOrPresent(message = "Data spaceru nie moze byc z przeszlosci.")
        LocalDate dataSpaceru,

        @NotNull(message = "Godzina rozpoczecia jest wymagana.")
        LocalTime godzinaStart,

        @NotNull(message = "Godzina zakonczenia jest wymagana.")
        LocalTime godzinaKoniec,

        String uwagi
) {
}

record RezerwacjaSpaceruResponse(
        Integer id,
        Integer wolontariuszId,
        String imieWolontariusza,
        String nazwiskoWolontariusza,
        Integer zwierzeId,
        String imieZwierzecia,
        LocalDate dataSpaceru,
        LocalTime godzinaStart,
        LocalTime godzinaKoniec,
        String uwagi,
        LocalDateTime dataZapisu
) {
}
