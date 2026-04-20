package com.turkusowi.backendapi.uzytkownicy;

import com.turkusowi.backendapi.common.ApiExceptionHandler;
import com.turkusowi.backendapi.common.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UzytkownikControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UzytkownikService uzytkownikService;

    @InjectMocks
    private UzytkownikController uzytkownikController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(uzytkownikController)
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
    }

    @Test
    void shouldPatchUserStatus() throws Exception {
        UpdateStatusUzytkownikaRequest request = new UpdateStatusUzytkownikaRequest(false);
        given(uzytkownikService.updateStatus(3, request)).willReturn(new UzytkownikResponse(
                3,
                "jan.kowalski@schronisko.pl",
                "Jan",
                "Kowalski",
                2,
                "PRACOWNIK",
                false,
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        ));

        mockMvc.perform(patch("/api/uzytkownicy/3/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "czyAktywny": false
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.czyAktywny").value(false));
    }

    @Test
    void shouldReturn404ForMissingUser() throws Exception {
        UpdateStatusUzytkownikaRequest request = new UpdateStatusUzytkownikaRequest(false);
        given(uzytkownikService.updateStatus(99, request))
                .willThrow(new NotFoundException("Nie znaleziono uzytkownika o id=99"));

        mockMvc.perform(patch("/api/uzytkownicy/99/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "czyAktywny": false
                                }
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Nie znaleziono uzytkownika o id=99"));
    }
}
