package com.turkusowi.backendapi.raporty;

import com.turkusowi.backendapi.common.ApiExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RaportOperacyjnyControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RaportOperacyjnyService raportOperacyjnyService;

    @InjectMocks
    private RaportOperacyjnyController raportOperacyjnyController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(raportOperacyjnyController)
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
    }

    @Test
    void shouldReturnOperationalReports() throws Exception {
        given(raportOperacyjnyService.findAll(null, null)).willReturn(List.of(
                new RaportOperacyjnyResponse(
                        1,
                        LocalDate.parse("2026-04-20"),
                        4,
                        "Piotr",
                        "Wisniewski",
                        "MIESIECZNY",
                        "Zwikszona liczba adopcji"
                )
        ));

        mockMvc.perform(get("/api/raporty-operacyjne"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].typRaportu").value("MIESIECZNY"));
    }
}
