package com.turkusowi.backendapi.auth;

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

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
    }

    @Test
    void shouldLoginUser() throws Exception {
        LoginRequest request = new LoginRequest("admin@schronisko.pl", "admin123");
        given(authService.login(request)).willReturn(new AuthUserResponse(
                1,
                "admin@schronisko.pl",
                "Anna",
                "Nowak",
                "ADMIN",
                true
        ));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "admin@schronisko.pl",
                                  "password": "admin123"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rola").value("ADMIN"))
                .andExpect(jsonPath("$.email").value("admin@schronisko.pl"));
    }

    @Test
    void shouldReturn409ForWrongCredentials() throws Exception {
        LoginRequest request = new LoginRequest("admin@schronisko.pl", "wrong");
        given(authService.login(request)).willThrow(new ConflictException("Niepoprawne dane logowania."));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "admin@schronisko.pl",
                                  "password": "wrong"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Niepoprawne dane logowania."));
    }
}
