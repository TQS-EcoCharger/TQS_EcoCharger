package pt.ua.tqs.ecocharger.ecocharger.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import pt.ua.tqs.ecocharger.ecocharger.dto.AuthResultDTO;
import pt.ua.tqs.ecocharger.ecocharger.models.Driver;
import pt.ua.tqs.ecocharger.ecocharger.models.User;
import pt.ua.tqs.ecocharger.ecocharger.repository.UserRepository;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.ChargingOperatorService;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.DriverService;
import pt.ua.tqs.ecocharger.ecocharger.utils.JwtUtil;
import pt.ua.tqs.ecocharger.ecocharger.utils.NotFoundException;
import app.getxray.xray.junit.customjunitxml.annotations.Requirement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

  @Mock private UserRepository userRepository;

  @Mock private DriverService driverService;

  @Mock private ChargingOperatorService chargingOperatorService;

  @Mock private JwtUtil jwtUtil;

  @InjectMocks private AuthenticationServiceImpl authService;

  private User enabledUser;

  @BeforeEach
  void setUp() {
    enabledUser = new User();
    enabledUser.setEmail("john@example.com");
    enabledUser.setPassword("123456");
    enabledUser.setEnabled(true);
    enabledUser.setName("John");
  }

  @Test
  @DisplayName("ET-49: Should login successfully and receive JWT")
  @Requirement("ET-49")
  void testSuccessfulLogin() {
    when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(enabledUser));
    when(jwtUtil.generateToken("john@example.com")).thenReturn("mocked-jwt");

    AuthResultDTO result = authService.authenticate("john@example.com", "123456");

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getMessage()).isEqualTo("Login successful");
    assertThat(result.getToken()).isEqualTo("mocked-jwt");
  }

  @Test
  @DisplayName("ET-49: Should fail login with wrong password")
  @Requirement("ET-49")
  void testWrongPassword() {
    when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(enabledUser));

    AuthResultDTO result = authService.authenticate("john@example.com", "wrongpass");

    assertThat(result.isSuccess()).isFalse();
    assertThat(result.getMessage()).isEqualTo("Invalid password");
    assertThat(result.getToken()).isNull();
  }

  @Test
  @DisplayName("ET-49: Should fail login with disabled user")
  @Requirement("ET-49")
  void testDisabledUser() {
    enabledUser.setEnabled(false);
    when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(enabledUser));

    AuthResultDTO result = authService.authenticate("john@example.com", "123456");

    assertThat(result.isSuccess()).isFalse();
    assertThat(result.getMessage()).isEqualTo("User is disabled");
    assertThat(result.getToken()).isNull();
  }

  @Test
  @DisplayName("ET-49: Should fail login with non-existent user")
  @Requirement("ET-49")
  void testNonExistentUser() {
    when(userRepository.findByEmail("nobody@example.com")).thenReturn(Optional.empty());

    AuthResultDTO result = authService.authenticate("nobody@example.com", "nopass");

    assertThat(result.isSuccess()).isFalse();
    assertThat(result.getMessage()).isEqualTo("User not found");
    assertThat(result.getToken()).isNull();
  }

  @Test
  @DisplayName("ET-52: Should register successfully and receive JWT")
  @Requirement("ET-52")
  void testSuccessfulRegistration() {
    when(userRepository.findByEmail("new_guy@example.com")).thenReturn(Optional.empty());
    when(jwtUtil.generateToken("new_guy@example.com")).thenReturn("reg-jwt");

    AuthResultDTO result = authService.register("new_guy@example.com", "123456", "New Guy");

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getMessage()).isEqualTo("Registration successful");
    assertThat(result.getToken()).isEqualTo("reg-jwt");
    verify(driverService).createDriver(any(Driver.class));
  }

  @Test
  @DisplayName("ET-52: Should fail registration with existing email")
  @Requirement("ET-52")
  void testExistingEmail() {
    when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(enabledUser));

    AuthResultDTO result = authService.register("john@example.com", "123456", "John Doe");

    assertThat(result.isSuccess()).isFalse();
    assertThat(result.getMessage()).isEqualTo("Email already in use");
  }

  @Test
  @DisplayName("ET-52: Should fail registration with short password")
  @Requirement("ET-52")
  void testShortPassword() {
    AuthResultDTO result = authService.register("new@example.com", "123", "New Guy");

    assertThat(result.isSuccess()).isFalse();
    assertThat(result.getMessage()).isEqualTo("Password must be at least 6 characters");
  }

  @Test
  @DisplayName("ET-52: Should fail registration with short name")
  @Requirement("ET-52")
  void testShortName() {
    AuthResultDTO result = authService.register("new@example.com", "123456", "ab");

    assertThat(result.isSuccess()).isFalse();
    assertThat(result.getMessage()).isEqualTo("Name must be at least 3 characters");
  }

  @Test
  @DisplayName("ET-52: Should fail registration with invalid email format")
  @Requirement("ET-52")
  void testInvalidEmailFormat() {
    AuthResultDTO result = authService.register("invalid-email", "123456", "New Guy");

    assertThat(result.isSuccess()).isFalse();
    assertThat(result.getMessage()).isEqualTo("Invalid email format");
  }

  @Test
  @DisplayName("ET-49: Should return correct user type as driver")
  @Requirement("ET-49")
  void testUserTypeAsDriver() {
    enabledUser.setId(0L);
    when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(enabledUser));
    when(driverService.driverExists(0L)).thenReturn(true);
    when(jwtUtil.generateToken("john@example.com")).thenReturn("driver-jwt");

    AuthResultDTO result = authService.authenticate("john@example.com", "123456");

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getUserType()).isEqualTo("driver");
  }

  @Test
  @DisplayName("ET-49: Should throw for invalid token format")
  @Requirement("ET-49")
  void testInvalidTokenFormat() {
    when(jwtUtil.getEmailFromToken(any()))
        .thenThrow(new IllegalArgumentException("Invalid token format"));

    assertThrows(IllegalArgumentException.class, () -> authService.getCurrentUser("bad.token"));
  }

  @Test
  @DisplayName("ET-49: Should throw when user not found")
  @Requirement("ET-49")
  void testUserNotFound() {
    when(jwtUtil.getEmailFromToken(any())).thenReturn("missing@example.com");
    when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> authService.getCurrentUser("valid.token"));
  }

  @Test
  @DisplayName("ET-49: Should throw when user is disabled")
  @Requirement("ET-49")
  void testDisabledUserInGetCurrentUser() {
    enabledUser.setEnabled(false);
    when(jwtUtil.getEmailFromToken(any())).thenReturn("john@example.com");
    when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(enabledUser));

    assertThrows(IllegalArgumentException.class, () -> authService.getCurrentUser("valid.token"));
  }
}
