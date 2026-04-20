package com.turkusowi.backendapi.rasy;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

record UpsertRasaRequest(
        @NotBlank(message = "Typ zwierzecia jest wymagany.")
        @Size(max = 100, message = "Typ zwierzecia moze miec maksymalnie 100 znakow.")
        String typZwierzecia,

        @NotBlank(message = "Nazwa rasy jest wymagana.")
        @Size(max = 100, message = "Nazwa rasy moze miec maksymalnie 100 znakow.")
        String rasa
) {
}

record RasaResponse(
        Integer id,
        String typZwierzecia,
        String rasa
) {
}
