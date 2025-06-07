package pt.ua.tqs.ecocharger.ecocharger.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pt.ua.tqs.ecocharger.ecocharger.config.SecurityDisableConfig;
import pt.ua.tqs.ecocharger.ecocharger.dto.ChargingSessionResponseDTO;
import pt.ua.tqs.ecocharger.ecocharger.dto.OtpValidationRequestDTO;
import pt.ua.tqs.ecocharger.ecocharger.dto.OtpValidationResponse;
import pt.ua.tqs.ecocharger.ecocharger.dto.StartChargingRequestDTO;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingSession;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingStatus;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.ChargingSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChargingSessionController.class)
@Import(SecurityDisableConfig.class)
class ChargingSessionControllerTest {

  @Autowired private MockMvc mockMvc;

  @SuppressWarnings("removal")
  @MockBean
  private ChargingSessionService chargingSessionService;

  @Autowired private ObjectMapper objectMapper;

  @Test
  @DisplayName("Valid OTP returns success")
  void testValidateOtpSuccess() throws Exception {
    OtpValidationRequestDTO request = new OtpValidationRequestDTO("123456", 1L);
    OtpValidationResponse response = new OtpValidationResponse(true, "OTP is valid.");

    Mockito.when(chargingSessionService.validateOtp("123456", 1L)).thenReturn(response);

    mockMvc
        .perform(
            post("/api/v1/sessions/validate-otp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.valid").value(true))
        .andExpect(jsonPath("$.reason").value("OTP is valid."));
  }

  @Test
  @DisplayName("Invalid OTP returns 400")
  void testValidateOtpFailure() throws Exception {
    OtpValidationRequestDTO request = new OtpValidationRequestDTO("000000", 2L);
    OtpValidationResponse response = new OtpValidationResponse(false, "Invalid OTP.");

    Mockito.when(chargingSessionService.validateOtp("000000", 2L)).thenReturn(response);

    mockMvc
        .perform(
            post("/api/v1/sessions/validate-otp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.valid").value(false))
        .andExpect(jsonPath("$.reason").value("Invalid OTP."));
  }

  @Test
  @DisplayName("Start session successfully")
  void testStartChargingSuccess() throws Exception {
    StartChargingRequestDTO request = new StartChargingRequestDTO();
    request.setOtp("123456");
    request.setChargingPointId(1L);
    request.setCarId(10L);

    ChargingSession mockSession = new ChargingSession();
    mockSession.setId(99L);

    Mockito.when(chargingSessionService.startSessionWithOtp(1L, "123456", 10L))
        .thenReturn(mockSession);

    mockMvc
        .perform(
            post("/api/v1/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(99L));
  }

  @Test
  @DisplayName("Start session with invalid OTP returns 400")
  void testStartChargingInvalidOtp() throws Exception {
    StartChargingRequestDTO request = new StartChargingRequestDTO();
    request.setOtp("badotp");
    request.setChargingPointId(1L);
    request.setCarId(10L);

    Mockito.when(chargingSessionService.startSessionWithOtp(1L, "badotp", 10L))
        .thenThrow(new IllegalArgumentException("Invalid OTP"));

    mockMvc
        .perform(
            post("/api/v1/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Invalid OTP"));
  }

  @Test
  @DisplayName("End session successfully")
  void testEndSessionSuccess() throws Exception {
    ChargingSession mockSession = new ChargingSession();
    mockSession.setId(101L);

    Mockito.when(chargingSessionService.endSession(101L)).thenReturn(mockSession);

    mockMvc
        .perform(post("/api/v1/sessions/101/end"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(101L));
  }

  @Test
  @DisplayName("End non-existent session returns 404")
  void testEndSessionNotFound() throws Exception {
    Mockito.when(chargingSessionService.endSession(202L))
        .thenThrow(new IllegalArgumentException("Session not found"));

    mockMvc
        .perform(post("/api/v1/sessions/202/end"))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Session not found"));
  }

  @Test
  @DisplayName("Get sessions by user returns 200 with data")
  void testGetSessionsByUserSuccess() throws Exception {
    List<ChargingSession> mockSessions = List.of(new ChargingSession());
    Mockito.when(chargingSessionService.getSessionsByUser(5L)).thenReturn(mockSessions);

    mockMvc.perform(get("/api/v1/sessions/user/5")).andExpect(status().isOk());
  }

  @Test
  @DisplayName("Get sessions by user returns 500 on exception")
  void testGetSessionsByUserFailure() throws Exception {
    Mockito.when(chargingSessionService.getSessionsByUser(999L))
        .thenThrow(new RuntimeException("Database down"));

    mockMvc
        .perform(get("/api/v1/sessions/user/999"))
        .andExpect(status().isInternalServerError())
        .andExpect(content().string(containsString("Database down")));
  }

  @Test
  @DisplayName("Get all charging sessions returns list")
  void testGetAllChargingSessionsSuccess() throws Exception {
    ChargingSessionResponseDTO dto = new ChargingSessionResponseDTO();
    dto.setId(1L);
    dto.setTotalCost(10.5);
    dto.setStatus(ChargingStatus.COMPLETED);

    Mockito.when(chargingSessionService.getAllSessions()).thenReturn(List.of(dto));

    mockMvc
        .perform(get("/api/v1/sessions"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1L))
        .andExpect(jsonPath("$[0].totalCost").value(10.5))
        .andExpect(jsonPath("$[0].status").value("COMPLETED"));
  }

  @Test
  @DisplayName("Get all charging sessions handles internal error")
  void testGetAllChargingSessionsError() throws Exception {
    Mockito.when(chargingSessionService.getAllSessions())
        .thenThrow(new RuntimeException("Database failure"));

    mockMvc
        .perform(get("/api/v1/sessions"))
        .andExpect(status().isInternalServerError())
        .andExpect(content().string("Error: Database failure"));
  }

  @Test
  @DisplayName("Get all sessions returns 500 on error")
  void testGetAllSessionsFailure() throws Exception {
    Mockito.when(chargingSessionService.getAllSessions())
        .thenThrow(new RuntimeException("Unexpected error"));

    mockMvc
        .perform(get("/api/v1/sessions"))
        .andExpect(status().isInternalServerError())
        .andExpect(content().string(containsString("Unexpected error")));
  }
}
