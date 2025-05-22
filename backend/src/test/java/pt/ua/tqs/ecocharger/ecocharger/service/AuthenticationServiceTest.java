package pt.ua.tqs.ecocharger.ecocharger.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import app.getxray.xray.junit.customjunitxml.annotations.Requirement;
import pt.ua.tqs.ecocharger.ecocharger.dto.AuthResultDTO;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.AuthenticationService;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class AuthenticationServiceTest {

  @Autowired private AuthenticationService authService;

  @Test
  @DisplayName("ET-49: Should login successfully and receive JWT")
  @Requirement("ET-49")
  void testSuccessfulLogin() {
    AuthResultDTO result = authService.authenticate("john@example.com", "123456");
    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getMessage()).isEqualTo("Login successful");
    assertThat(result.getToken()).isNotNull();
  }

  @Test
  @DisplayName("ET-49: Should fail login with wrong password")
  @Requirement("ET-49")
  void testWrongPassword() {
    AuthResultDTO result = authService.authenticate("john@example.com", "wrongpass");
    assertThat(result.isSuccess()).isFalse();
    assertThat(result.getMessage()).isEqualTo("Invalid password");
    assertThat(result.getToken()).isNull();
  }

  @Test
  @DisplayName("ET-49: Should fail login with disabled user")
  @Requirement("ET-49")
  void testDisabledUser() {
    AuthResultDTO result = authService.authenticate("bob@example.com", "bobpass");
    assertThat(result.isSuccess()).isFalse();
    assertThat(result.getMessage()).isEqualTo("User is disabled");
    assertThat(result.getToken()).isNull();
  }

  @Test
  @DisplayName("ET-49: Should fail login with non-existent user")
  @Requirement("ET-49")
  void testNonExistentUser() {
    AuthResultDTO result = authService.authenticate("nobody@example.com", "nopass");
    assertThat(result.isSuccess()).isFalse();
    assertThat(result.getMessage()).isEqualTo("User not found");
    assertThat(result.getToken()).isNull();
  }

  @Test
  @DisplayName("ET-52: Should register successfully and receive JWT")
  @Requirement("ET-52")
  void testSuccessfulRegistration() {
    AuthResultDTO result = authService.register("new_guy@example.com", "123456", "New Guy");
    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getMessage()).isEqualTo("Registration successful");
    assertThat(result.getToken()).isNotNull();
  }

  @Test
  @DisplayName("ET-52: Should fail registration with existing email")
  @Requirement("ET-52")
  void testExistingEmail() {
    AuthResultDTO result = authService.register("john@example.com", "123456", "John Doe");
    assertThat(result.isSuccess()).isFalse();
    assertThat(result.getMessage()).isEqualTo("Email already in use");
    assertThat(result.getToken()).isNull();
  }

  @Test
  @DisplayName("ET-52: Should fail registration with short password")
  @Requirement("ET-52")
  void testShortPassword() {
    AuthResultDTO result = authService.register("new_guy1@example.com", "123", "New Guy");
    assertThat(result.isSuccess()).isFalse();
    assertThat(result.getMessage()).isEqualTo("Password must be at least 6 characters");
    assertThat(result.getToken()).isNull();
  }

  @Test
  @DisplayName("ET-52: Should fail registration with short name")
  @Requirement("ET-52")
  void testEmptyName() {
    AuthResultDTO result = authService.register("new_guy2@example.com", "123456", "ab");
    assertThat(result.isSuccess()).isFalse();
    assertThat(result.getMessage()).isEqualTo("Name must be at least 3 characters");
    assertThat(result.getToken()).isNull();
  }

  @Test
  @DisplayName("ET-52: Should fail registration with invalid email format")
  @Requirement("ET-52")
  void testInvalidEmailFormat() {
    AuthResultDTO result = authService.register("invalid-email", "123456", "New Guy");
    assertThat(result.isSuccess()).isFalse();
    assertThat(result.getMessage()).isEqualTo("Invalid email format");
    assertThat(result.getToken()).isNull();
  }
}
