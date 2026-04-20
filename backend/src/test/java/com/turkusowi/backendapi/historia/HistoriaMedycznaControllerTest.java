package com.turkusowi.backendapi.historia;

import com.turkusowi.backendapi.common.ApiExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class HistoriaMedycznaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private HistoriaMedycznaService historiaMedycznaService;

    @InjectMocks
    private HistoriaMedycznaController historiaMedycznaController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(historiaMedycznaController)
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
    }

    @Test
    void shouldReturnMedicalHistoryForAnimal() throws Exception {
        given(historiaMedycznaService.findAll(2, null)).willReturn(List.of(
                new HistoriaMedycznaResponse(
                        11,
                        2,
                        "Luna",
                        7,
                        "Jan",
                        "Kowalski",
                        "Przeglad weterynaryjny",
                        LocalDateTime.parse("2026-04-20T10:15:00")
                )
        ));

        mockMvc.perform(get("/api/historia-medyczna").param("zwierzeId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].imieZwierzecia").value("Luna"))
                .andExpect(jsonPath("$[0].opisZabiegu").value("Przeglad weterynaryjny"));
    }
}
