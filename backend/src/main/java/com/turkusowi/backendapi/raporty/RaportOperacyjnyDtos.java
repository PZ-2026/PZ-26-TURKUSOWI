package com.turkusowi.backendapi.raporty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

record UpsertRaportOperacyjnyRequest(
        @NotNull(message = "Data raportu jest wymagana.")
        LocalDate dataRaportu,

        @NotNull(message = "Id sekretarza jest wymagane.")
        Integer sekretarzId,

        @NotBlank(message = "Typ raportu jest wymagany.")
        @Size(max = 100, message = "Typ raportu moze miec maksymalnie 100 znakow.")
        String typRaportu,

        String uwagi
) {
}

record RaportOperacyjnyResponse(
        Integer id,
        LocalDate dataRaportu,
        Integer sekretarzId,
        String imieSekretarza,
        String nazwiskoSekretarza,
        String typRaportu,
        String uwagi
) {
}
