package com.turkusowi.backendapi.historia;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

record UpsertHistoriaMedycznaRequest(
        @NotNull(message = "Id zwierzecia jest wymagane.")
        Integer zwierzeId,

        @NotNull(message = "Id pracownika jest wymagane.")
        Integer pracownikId,

        @NotBlank(message = "Opis zabiegu jest wymagany.")
        String opisZabiegu,

        LocalDateTime dataWpisu
) {
}

record HistoriaMedycznaResponse(
        Integer id,
        Integer zwierzeId,
        String imieZwierzecia,
        Integer pracownikId,
        String imiePracownika,
        String nazwiskoPracownika,
        String opisZabiegu,
        LocalDateTime dataWpisu
) {
}
