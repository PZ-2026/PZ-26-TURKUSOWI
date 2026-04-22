package com.turkusowi.backendapi.zwierzeta;

import com.turkusowi.backendapi.common.ApiExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ZwierzeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ZwierzeService zwierzeService;

    @InjectMocks
    private ZwierzeController zwierzeController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(zwierzeController)
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
    }

    @Test
    void shouldReturnPublicAnimalsForGuests() throws Exception {
        given(zwierzeService.findPubliczne()).willReturn(List.of(
                new ZwierzePublicResponse(1, "Burek", 5, "Owczarek Niemiecki", "Pies", "DO ADOPCJI", "Duzy pies", "Przyjacielski")
        ));

        mockMvc.perform(get("/api/zwierzeta/publiczne"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].imie").value("Burek"))
                .andExpect(jsonPath("$[0].status").value("DO ADOPCJI"));
    }

    @Test
    void shouldCreateAnimal() throws Exception {
        UpsertZwierzeRequest request = new UpsertZwierzeRequest(
                "Luna",
                2,
                new BigDecimal("4.20"),
                "Samica",
                3,
                "DO ADOPCJI",
                "Spokojna kotka",
                "Lagodna",
                "Karma mokra"
        );

        given(zwierzeService.create(request)).willReturn(new ZwierzeResponse(
                8,
                "Luna",
                2,
                new BigDecimal("4.20"),
                "Samica",
                3,
                "Brytyjski Krotkowlosy",
                "Kot",
                "DO ADOPCJI",
                "Spokojna kotka",
                "Lagodna",
                "Karma mokra",
                LocalDateTime.now()
        ));

        mockMvc.perform(post("/api/zwierzeta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "imie": "Luna",
                                  "wiek": 2,
                                  "waga": 4.20,
                                  "plec": "Samica",
                                  "rasaId": 3,
                                  "status": "DO ADOPCJI",
                                  "opis": "Spokojna kotka",
                                  "charakter": "Lagodna",
                                  "preferencjeZywieniowe": "Karma mokra"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(8))
                .andExpect(jsonPath("$.rasa").value("Brytyjski Krotkowlosy"));
    }
}
