package com.turkusowi.backendapi.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

record LoginRequest(
        @NotBlank(message = "Email jest wymagany.")
        @Email(message = "Podaj poprawny adres email.")
        String email,

        @NotBlank(message = "Haslo jest wymagane.")
        String password
) {
}

record RegisterRequest(
        @NotBlank(message = "Imie jest wymagane.")
        @Size(max = 100, message = "Imie moze miec maksymalnie 100 znakow.")
        String imie,

        @NotBlank(message = "Nazwisko jest wymagane.")
        @Size(max = 100, message = "Nazwisko moze miec maksymalnie 100 znakow.")
        String nazwisko,

        @NotBlank(message = "Email jest wymagany.")
        @Email(message = "Podaj poprawny adres email.")
        @Size(max = 255, message = "Email moze miec maksymalnie 255 znakow.")
        String email,

        @NotBlank(message = "Haslo jest wymagane.")
        @Size(min = 8, message = "Haslo musi miec minimum 8 znakow.")
        @Size(max = 255, message = "Haslo moze miec maksymalnie 255 znakow.")
        String password
) {
}

record ForgotPasswordRequest(
        @NotBlank(message = "Email jest wymagany.")
        @Email(message = "Podaj poprawny adres email.")
        String email
) {
}

record AuthUserResponse(
        Integer id,
        String email,
        String imie,
        String nazwisko,
        String rola,
        boolean czyAktywny
) {
}

record AuthMessageResponse(
        String message
) {
}
