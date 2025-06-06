package pt.ua.tqs.ecocharger.ecocharger.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pt.ua.tqs.ecocharger.ecocharger.config.SecurityDisableConfig;
import pt.ua.tqs.ecocharger.ecocharger.dto.AuthResultDTO;
import pt.ua.tqs.ecocharger.ecocharger.models.User;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.web.servlet.MockMvc;
import app.getxray.xray.junit.customjunitxml.annotations.Requirement;
import pt.ua.tqs.ecocharger.ecocharger.utils.NotFoundException;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

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
  @Requirement("ET-52")
  void testLoginSuccess() throws Exception {
    Mockito.when(authService.authenticate("john@example.com", "123456"))
        .thenReturn(new AuthResultDTO(true, "Login successful", "token123", "administrator"));

    String requestBody =
        """
          {
            "email": "john@example.com",
            "password": "123456"
          }
        """;

    mockMvc
        .perform(post("/api/auth/login").contentType("application/json").content(requestBody))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Login successful"))
        .andExpect(jsonPath("$.token").value("token123"))
        .andExpect(jsonPath("$.userType").value("administrator"));
  }

  @Test
  @DisplayName("Login failure returns 401 Unauthorized and message only")
  @Requirement("ET-52")
  void testLoginFailure() throws Exception {
    Mockito.when(authService.authenticate("john@example.com", "wrongpass"))
        .thenReturn(new AuthResultDTO(false, "Invalid password", null, null));

    String requestBody =
        """
          {
            "email": "john@example.com",
            "password": "wrongpass"
          }
        """;

    mockMvc
        .perform(post("/api/auth/login").contentType("application/json").content(requestBody))
        .andExpect(status().isUnauthorized())
        .andExpect(content().string(containsString("Invalid password")));
  }

  @Test
  @DisplayName("Register success returns 200 OK and token in JSON")
  @Requirement("ET-52")
  void testRegisterSuccess() throws Exception {
    Mockito.when(authService.register("mariah@example.com", "123456", "Mariah"))
        .thenReturn(new AuthResultDTO(true, "Registration successful", "token123", "driver"));

    String requestBody =
        "{ \"email\": \"mariah@example.com\", \"password\": \"123456\", \"name\": \"Mariah\" }";

    mockMvc
        .perform(post("/api/auth/register").content(requestBody).contentType("application/json"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Registration successful"))
        .andExpect(jsonPath("$.token").value("token123"))
        .andExpect(jsonPath("$.userType").value("driver"));
  }

  @Test
  @DisplayName("Registering with existing email returns 400 Bad Request and message only")
  @Requirement("ET-52")
  void testRegisterExistingEmail() throws Exception {
    Mockito.when(authService.register("peter@example.com", "123456", "Peter"))
        .thenReturn(new AuthResultDTO(false, "Email already in use", null, null));

    String requestBody =
        "{ \"email\": \" peter@example.com\", \"password\": \"123456\", \"name\": \"Peter\" }";

    mockMvc
        .perform(post("/api/auth/register").content(requestBody).contentType("application/json"))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("Email already in use")));
  }

  @Test
  @DisplayName("Registering with short password returns 400 Bad Request and message only")
  @Requirement("ET-52")
  void testRegisterShortPassword() throws Exception {
    Mockito.when(authService.register("andrew@example.com", "123", "Andrew"))
        .thenReturn(new AuthResultDTO(false, "Password must be at least 6 characters", null, null));

    String requestBody =
        "{ \"email\": \" andrew@example.com\", \"password\": \"123\", \"name\": \"Andrew\" }";

    mockMvc
        .perform(post("/api/auth/register").content(requestBody).contentType("application/json"))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("Password must be at least 6 characters")));
  }

  @Test
  @DisplayName("Registering with invalid email returns 400 Bad Request and message only")
  @Requirement("ET-52")
  void testRegisterInvalidEmail() throws Exception {
    Mockito.when(authService.register("invalid-email", "123456", "Invalid"))
        .thenReturn(new AuthResultDTO(false, "Invalid email format", null, null));
    String requestBody =
        "{ \"email\": \" invalid-email\", \"password\": \"123456\", \"name\": \"Invalid\" }";

    mockMvc
        .perform(post("/api/auth/register").content(requestBody).contentType("application/json"))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("Invalid email format")));
  }

  @Test
  @DisplayName("Registering with invalid name returns 400 Bad Request and message only")
  @Requirement("ET-52")
  void testRegisterInvalidName() throws Exception {
    Mockito.when(authService.register("john@example.com", "123456", ""))
        .thenReturn(new AuthResultDTO(false, "Name must be at least 3 characters", null, null));
    String requestBody =
        "{ \"email\": \" john@example.com\", \"password\": \"123456\", \"name\": \"\" }";

    mockMvc
        .perform(post("/api/auth/register").content(requestBody).contentType("application/json"))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("Name must be at least 3 characters")));
  }

  @Test
  @DisplayName("User details retrieval success returns 200 OK and user details in JSON")
  @Requirement("ET-52")
  void testGetCurrentUserNotFound() throws Exception {
    when(authService.getCurrentUser("unknown.token"))
        .thenThrow(new NotFoundException("User not found"));

    mockMvc
        .perform(get("/api/auth/me").header("Authorization", "Bearer unknown.token"))
        .andExpect(status().isForbidden())
        .andExpect(content().string("User not found"));
  }

  @Test
  @DisplayName("Registering with missing fields returns 400 Bad Request and error message")
  @Requirement("ET-52")
  void testRegisterMissingFields() throws Exception {
    String requestBody = "{ \"email\": \"user@example.com\" }"; // falta password e name

    mockMvc
        .perform(post("/api/auth/register").contentType("application/json").content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("Missing required fields")));
  }

  @Test
  @DisplayName("Get current user returns 200 OK and user info")
  @Requirement("ET-52")
  void testGetCurrentUserSuccess() throws Exception {
    User mockUser = new User();
    mockUser.setId(1L);
    mockUser.setEmail("user@example.com");

    Mockito.when(authService.getCurrentUser("valid.token")).thenReturn(mockUser);

    mockMvc
        .perform(get("/api/auth/me").header("Authorization", "Bearer valid.token"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value("user@example.com"));
  }

  @Test
  @DisplayName("Get current user with invalid token throws 401")
  @Requirement("ET-52")
  void testGetCurrentUserInvalidToken() throws Exception {
    Mockito.when(authService.getCurrentUser(""))
        .thenThrow(new IllegalArgumentException("Invalid token"));

    mockMvc
        .perform(get("/api/auth/me").header("Authorization", "Bearer "))
        .andExpect(status().isUnauthorized())
        .andExpect(content().string("Invalid token"));
  }
}
