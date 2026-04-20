package com.turkusowi.backendapi.uzytkownicy;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

record CreateUzytkownikRequest(
        @NotBlank(message = "Email jest wymagany.")
        @Email(message = "Podaj poprawny adres email.")
        @Size(max = 255, message = "Email moze miec maksymalnie 255 znakow.")
        String email,

        @NotBlank(message = "Haslo hash jest wymagane.")
        @Size(max = 255, message = "Haslo hash moze miec maksymalnie 255 znakow.")
        String hasloHash,

        @NotBlank(message = "Imie jest wymagane.")
        @Size(max = 100, message = "Imie moze miec maksymalnie 100 znakow.")
        String imie,

        @NotBlank(message = "Nazwisko jest wymagane.")
        @Size(max = 100, message = "Nazwisko moze miec maksymalnie 100 znakow.")
        String nazwisko,

        @NotNull(message = "Rola jest wymagana.")
        Integer rolaId,

        boolean czyAktywny
) implements UzytkownikService.BaseUzytkownikRequest {
}

record UpdateUzytkownikRequest(
        @NotBlank(message = "Email jest wymagany.")
        @Email(message = "Podaj poprawny adres email.")
        @Size(max = 255, message = "Email moze miec maksymalnie 255 znakow.")
        String email,

        @NotBlank(message = "Haslo hash jest wymagane.")
        @Size(max = 255, message = "Haslo hash moze miec maksymalnie 255 znakow.")
        String hasloHash,

        @NotBlank(message = "Imie jest wymagane.")
        @Size(max = 100, message = "Imie moze miec maksymalnie 100 znakow.")
        String imie,

        @NotBlank(message = "Nazwisko jest wymagane.")
        @Size(max = 100, message = "Nazwisko moze miec maksymalnie 100 znakow.")
        String nazwisko,

        @NotNull(message = "Rola jest wymagana.")
        Integer rolaId,

        boolean czyAktywny
) implements UzytkownikService.BaseUzytkownikRequest {
}

record UpdateStatusUzytkownikaRequest(
        boolean czyAktywny
) {
}

record UzytkownikResponse(
        Integer id,
        String email,
        String imie,
        String nazwisko,
        Integer rolaId,
        String rolaNazwa,
        boolean czyAktywny,
        LocalDateTime ostatnieLogowanie,
        LocalDateTime dataUtworzenia,
        LocalDateTime dataModyfikacji
) {
}
