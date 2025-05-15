package pt.ua.tqs.ecocharger.ecocharger.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pt.ua.tqs.ecocharger.ecocharger.dto.AuthResultDTO;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.AuthenticationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import app.getxray.xray.junit.customjunitxml.annotations.Requirement;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthenticationController.class)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authService;

    @Test
    @Requirement("ET-49")
    @DisplayName("Login success returns 200 OK")
    void testLoginSuccess() throws Exception {
        Mockito.when(authService.authenticate("john@example.com", "123456"))
                .thenReturn(new AuthResultDTO(true, "Login successful"));

        mockMvc.perform(post("/api/auth/login")
                        .param("email", "john@example.com")
                        .param("password", "123456"))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful"));
    }

    @Test
    @Requirement("ET-49")
    @DisplayName("Login failure returns 401 Unauthorized")
    void testLoginFailure() throws Exception {
        Mockito.when(authService.authenticate("john@example.com", "wrongpass"))
                .thenReturn(new AuthResultDTO(false, "Invalid password"));

        mockMvc.perform(post("/api/auth/login")
                        .param("email", "john@example.com")
                        .param("password", "wrongpass"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid password"));
    }
}

