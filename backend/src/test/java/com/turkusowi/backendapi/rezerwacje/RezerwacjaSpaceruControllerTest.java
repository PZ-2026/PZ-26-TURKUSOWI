package com.turkusowi.backendapi.rezerwacje;

import com.turkusowi.backendapi.common.ApiExceptionHandler;
import com.turkusowi.backendapi.common.ConflictException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RezerwacjaSpaceruControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RezerwacjaSpaceruService rezerwacjaSpaceruService;

    @InjectMocks
    private RezerwacjaSpaceruController rezerwacjaSpaceruController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(rezerwacjaSpaceruController)
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
    }

    @Test
    void shouldCreateReservation() throws Exception {
        UpsertRezerwacjaSpaceruRequest request = new UpsertRezerwacjaSpaceruRequest(
                3,
                1,
                LocalDate.now().plusDays(1),
                LocalTime.of(10, 0),
                LocalTime.of(11, 0),
                "Spokojny spacer"
        );

        given(rezerwacjaSpaceruService.create(request)).willReturn(new RezerwacjaSpaceruResponse(
                14,
                3,
                "Marta",
                "Zielinska",
                1,
                "Burek",
                request.dataSpaceru(),
                request.godzinaStart(),
                request.godzinaKoniec(),
                request.uwagi(),
                LocalDateTime.now()
        ));

        mockMvc.perform(post("/api/rezerwacje-spacerow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "wolontariuszId": 3,
                                  "zwierzeId": 1,
                                  "dataSpaceru": "%s",
                                  "godzinaStart": "10:00:00",
                                  "godzinaKoniec": "11:00:00",
                                  "uwagi": "Spokojny spacer"
                                }
                                """.formatted(request.dataSpaceru())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.imieZwierzecia").value("Burek"));
    }

    @Test
    void shouldReturn409ForConflictingReservation() throws Exception {
        UpsertRezerwacjaSpaceruRequest request = new UpsertRezerwacjaSpaceruRequest(
                3,
                1,
                LocalDate.now().plusDays(1),
                LocalTime.of(10, 0),
                LocalTime.of(11, 0),
                "Kolizja"
        );

        given(rezerwacjaSpaceruService.create(request))
                .willThrow(new ConflictException("Wybrany termin dla tego zwierzecia jest juz zajety."));

        mockMvc.perform(post("/api/rezerwacje-spacerow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "wolontariuszId": 3,
                                  "zwierzeId": 1,
                                  "dataSpaceru": "%s",
                                  "godzinaStart": "10:00:00",
                                  "godzinaKoniec": "11:00:00",
                                  "uwagi": "Kolizja"
                                }
                                """.formatted(request.dataSpaceru())))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Wybrany termin dla tego zwierzecia jest juz zajety."));
    }
}
