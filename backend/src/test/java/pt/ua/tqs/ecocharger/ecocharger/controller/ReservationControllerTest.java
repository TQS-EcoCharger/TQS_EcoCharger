package pt.ua.tqs.ecocharger.ecocharger.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import app.getxray.xray.junit.customjunitxml.annotations.Requirement;
import pt.ua.tqs.ecocharger.ecocharger.config.SecurityDisableConfig;
import pt.ua.tqs.ecocharger.ecocharger.dto.ChargingPointDTO;
import pt.ua.tqs.ecocharger.ecocharger.dto.ChargingStationDTO;
import pt.ua.tqs.ecocharger.ecocharger.dto.ConnectorDTO;
import pt.ua.tqs.ecocharger.ecocharger.dto.ReservationRequestDTO;

import pt.ua.tqs.ecocharger.ecocharger.dto.ReservationResponseDTO;
import pt.ua.tqs.ecocharger.ecocharger.models.ReservationStatus;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.ReservationService;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.OTPService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationController.class)
@Import(SecurityDisableConfig.class)
public class ReservationControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired @MockitoBean private ReservationService reservationService;

  @MockitoBean @Autowired private OTPService otpService;

  private ReservationResponseDTO reservationResponse;

  @BeforeEach
  void setUp() {
    ChargingStationDTO chargingStation =
        new ChargingStationDTO(1L, "Station A", "Location A", 50.1234, -8.1234);
    List<ConnectorDTO> connectors = List.of(new ConnectorDTO(1L, "Type 2", 50, 2, 3));
    ChargingPointDTO chargingPoint =
        new ChargingPointDTO(2L, "ChargingBrand", true, 100.0, 10.0, connectors, chargingStation);
    reservationResponse =
        new ReservationResponseDTO(
            1L,
            1L,
            chargingPoint,
            LocalDateTime.parse("2023-05-28T10:00:00"),
            LocalDateTime.parse("2023-05-28T12:00:00"),
            ReservationStatus.TO_BE_USED);

    when(reservationService.getAllReservations()).thenReturn(List.of(reservationResponse));
    when(reservationService.getReservationsByUserId(1L)).thenReturn(List.of(reservationResponse));
    when(reservationService.getActiveReservationsByChargingPointId(2L))
        .thenReturn(List.of(reservationResponse));
    when(reservationService.createReservation(any(ReservationRequestDTO.class)))
        .thenReturn(reservationResponse);
  }

  @Test
  @Requirement("ET-30")
  @DisplayName("Creating reservation returns 200 OK with reservation details")
  void testCreateReservation() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/reservation")
                .contentType("application/json")
                .content(
                    "{\"userId\":1, \"chargingPointId\":2, \"startTime\":\"2023-05-28T10:00:00\","
                        + " \"endTime\":\"2023-05-28T12:00:00\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1));
  }

  @Test
  @Requirement("ET-30")
  @DisplayName("Getting all reservations returns 200 OK with reservations list")
  void testGetAllReservations() throws Exception {
    mockMvc
        .perform(get("/api/v1/reservation"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id").value(1));
  }

  @Test
  @Requirement("ET-30")
  @DisplayName("Getting reservations by user ID returns 200 OK with reservations list")
  void testGetReservationsByUserId() throws Exception {
    mockMvc
        .perform(get("/api/v1/reservation/user/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].userId").value(1));
  }

  @Test
  @Requirement("ET-30")
  @DisplayName(
      "Getting active reservations by charging point ID returns 200 OK with reservations list")
  void testGetActiveReservationsByChargingPoint() throws Exception {
    mockMvc
        .perform(get("/api/v1/reservation/point/2/active"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].chargingPoint.id").value(2));
  }
}
