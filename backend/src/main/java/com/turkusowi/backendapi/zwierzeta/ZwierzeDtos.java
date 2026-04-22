package com.turkusowi.backendapi.zwierzeta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

record UpsertZwierzeRequest(
        @NotBlank(message = "Imie zwierzecia jest wymagane.")
        @Size(max = 100, message = "Imie zwierzecia moze miec maksymalnie 100 znakow.")
        String imie,

        @PositiveOrZero(message = "Wiek nie moze byc ujemny.")
        Integer wiek,

        @PositiveOrZero(message = "Waga nie moze byc ujemna.")
        BigDecimal waga,

        @Size(max = 50, message = "Polec moze miec maksymalnie 50 znakow.")
        String plec,

        Integer rasaId,

        @Size(max = 100, message = "Status moze miec maksymalnie 100 znakow.")
        String status,

        String opis,
        String charakter,
        String preferencjeZywieniowe
) {
}

record ZwierzeResponse(
        Integer id,
        String imie,
        Integer wiek,
        BigDecimal waga,
        String plec,
        Integer rasaId,
        String rasa,
        String typZwierzecia,
        String status,
        String opis,
        String charakter,
        String preferencjeZywieniowe,
        LocalDateTime dataModyfikacji
) {
}

record ZwierzePublicResponse(
        Integer id,
        String imie,
        Integer wiek,
        String rasa,
        String typZwierzecia,
        String status,
        String opis,
        String charakter
) {
}
