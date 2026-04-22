package com.turkusowi.backendapi.rasy;

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

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RasaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RasaService rasaService;

    @InjectMocks
    private RasaController rasaController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(rasaController)
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
    }

    @Test
    void shouldReturnBreedsList() throws Exception {
        given(rasaService.findAll()).willReturn(List.of(
                new RasaResponse(1, "Pies", "Labrador")
        ));

        mockMvc.perform(get("/api/rasy"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].typZwierzecia").value("Pies"))
                .andExpect(jsonPath("$[0].rasa").value("Labrador"));
    }

    @Test
    void shouldCreateBreed() throws Exception {
        UpsertRasaRequest request = new UpsertRasaRequest("Kot", "Dachowiec");
        given(rasaService.create(request)).willReturn(new RasaResponse(5, "Kot", "Dachowiec"));

        mockMvc.perform(post("/api/rasy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "typZwierzecia": "Kot",
                                  "rasa": "Dachowiec"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.rasa").value("Dachowiec"));
    }
}
