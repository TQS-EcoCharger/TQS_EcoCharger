package pt.ua.tqs.ecocharger.ecocharger.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import pt.ua.tqs.ecocharger.ecocharger.config.SecurityDisableConfig;
import pt.ua.tqs.ecocharger.ecocharger.dto.AuthResultDTO;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.AuthenticationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthenticationController.class)
@Import(SecurityDisableConfig.class)
class AuthenticationControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private AuthenticationService authService;

  @TestConfiguration
  static class MockServiceConfig {
    @Bean
    @Primary
    public AuthenticationService authService() {
      return mock(AuthenticationService.class);
    }
  }

  @Test
  @DisplayName("Login success returns 200 OK and token in JSON")
  void testLoginSuccess() throws Exception {
    Mockito.when(authService.authenticate("john@example.com", "123456"))
        .thenReturn(new AuthResultDTO(true, "Login successful", "token123"));

    mockMvc
        .perform(
            post("/api/auth/login").param("email", "john@example.com").param("password", "123456"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Login successful"))
        .andExpect(jsonPath("$.token").value("token123"));
  }

  @Test
  @DisplayName("Login failure returns 401 Unauthorized and message only")
  void testLoginFailure() throws Exception {
    Mockito.when(authService.authenticate("john@example.com", "wrongpass"))
        .thenReturn(new AuthResultDTO(false, "Invalid password", null));

    mockMvc
        .perform(
            post("/api/auth/login")
                .param("email", "john@example.com")
                .param("password", "wrongpass"))
        .andExpect(status().isUnauthorized())
        .andExpect(content().string(containsString("Invalid password")));
  }
}
