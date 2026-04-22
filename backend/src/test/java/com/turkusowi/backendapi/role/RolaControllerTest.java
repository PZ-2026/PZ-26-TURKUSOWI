package com.turkusowi.backendapi.role;

import com.turkusowi.backendapi.common.ApiExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RolaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RolaService rolaService;

    @InjectMocks
    private RolaController rolaController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(rolaController)
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
    }

    @Test
    void shouldReturnRolesList() throws Exception {
        given(rolaService.findAll()).willReturn(List.of(
                new RolaResponse(1, "ADMIN"),
                new RolaResponse(2, "WOLONTARIUSZ")
        ));

        mockMvc.perform(get("/api/role"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nazwa").value("ADMIN"))
                .andExpect(jsonPath("$[1].nazwa").value("WOLONTARIUSZ"));
    }
}
