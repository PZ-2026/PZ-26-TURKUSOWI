package com.turkusowi.backendapi.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class SecurityConfigTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void shouldAllowGuestOnlyPublicAnimalPreview() throws Exception {
        mockMvc.perform(get("/api/zwierzeta/publiczne"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/zwierzeta"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/role"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "WOLONTARIUSZ")
    void shouldAllowVolunteerToViewAnimalsAndReserveWalksOnly() throws Exception {
        mockMvc.perform(get("/api/zwierzeta"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/rezerwacje-spacerow"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/zwierzeta"))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/api/historia-medyczna"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "PRACOWNIK")
    void shouldAllowEmployeeToUseStaffEndpointsButNotUserManagement() throws Exception {
        mockMvc.perform(get("/api/zwierzeta"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/zwierzeta"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/api/uzytkownicy"))
                .andExpect(status().isForbidden());

        mockMvc.perform(patch("/api/uzytkownicy/1/status"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldAllowAdminToAccessUserManagement() throws Exception {
        mockMvc.perform(get("/api/uzytkownicy"))
                .andExpect(status().isOk());
    }
}
