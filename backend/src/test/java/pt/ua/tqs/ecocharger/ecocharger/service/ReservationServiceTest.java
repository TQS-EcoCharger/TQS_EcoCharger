package pt.ua.tqs.ecocharger.ecocharger.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import app.getxray.xray.junit.customjunitxml.annotations.Requirement;
import pt.ua.tqs.ecocharger.ecocharger.dto.ReservationRequestDTO;
import pt.ua.tqs.ecocharger.ecocharger.dto.ReservationResponseDTO;
import pt.ua.tqs.ecocharger.ecocharger.models.*;
import pt.ua.tqs.ecocharger.ecocharger.repository.*;

public class ReservationServiceTest {

  @Mock private ReservationRepository reservationRepository;

  @Mock private UserRepository userRepository;

  @Mock private ChargingPointRepository chargingPointRepository;

  @InjectMocks private ReservationServiceImpl reservationService;

  private User user;
  private ChargingPoint chargingPoint;
  private Reservation reservation;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    user = new User(1L, "user@example.com", "password", "User", true);

    List<Connectors> connectors =
        List.of(
            new Connectors("Type1", 1, 5, 10, "Voltage"),
            new Connectors("Type2", 7, 15, 22, "Voltage"));

    ChargingStation station = new ChargingStation("City", "Address", 10.0, 20.0, "2000", "Country");

    chargingPoint = new ChargingPoint(station, true, "BrandX", connectors);
    chargingPoint.setId(2L);

    reservation =
        new Reservation(
            1L,
            user,
            chargingPoint,
            LocalDateTime.parse("2023-05-28T10:00:00"),
            LocalDateTime.parse("2023-05-28T12:00:00"),
            ReservationStatus.TO_BE_USED);

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(chargingPointRepository.findById(2L)).thenReturn(Optional.of(chargingPoint));
    when(reservationRepository.existsByChargingPointIdAndTimeOverlap(
            2L, reservation.getStartTime(), reservation.getEndTime()))
        .thenReturn(false);
    when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
  }

  @Test
  @DisplayName("Create Reservation Successfully")
  @Requirement("ET-30")
  void testCreateReservation() {
    ReservationRequestDTO request =
        new ReservationRequestDTO(
            1L,
            2L,
            LocalDateTime.parse("2023-05-28T10:00:00"),
            LocalDateTime.parse("2023-05-28T12:00:00"));

    ReservationResponseDTO response = reservationService.createReservation(request);

    assertNotNull(response);
    assertEquals(1L, response.getId());
    verify(reservationRepository, times(1)).save(any(Reservation.class));
  }

  @Test
  @DisplayName("Fail to Create Reservation - Time Slot Conflict")
  @Requirement("ET-30")
  void testCreateReservationConflict() {
    when(reservationRepository.existsByChargingPointIdAndTimeOverlap(anyLong(), any(), any()))
        .thenReturn(true);

    ReservationRequestDTO request =
        new ReservationRequestDTO(
            1L,
            2L,
            LocalDateTime.parse("2023-05-28T10:00:00"),
            LocalDateTime.parse("2023-05-28T12:00:00"));

    assertThrows(IllegalStateException.class, () -> reservationService.createReservation(request));
    verify(reservationRepository, times(0)).save(any(Reservation.class));
  }

  @Test
  @DisplayName("Get All Reservations")
  @Requirement("ET-30")
  void testGetAllReservations() {
    when(reservationRepository.findAll()).thenReturn(List.of(reservation));

    List<ReservationResponseDTO> responses = reservationService.getAllReservations();

    assertEquals(1, responses.size());
    assertEquals(1L, responses.get(0).getId());
  }

  @Test
  @DisplayName("Get Reservations by User ID")
  @Requirement("ET-30")
  void testGetReservationsByUserId() {
    when(reservationRepository.findByUserId(1L)).thenReturn(List.of(reservation));

    List<ReservationResponseDTO> responses = reservationService.getReservationsByUserId(1L);

    assertEquals(1, responses.size());
    assertEquals(1L, responses.get(0).getUserId());
  }

  @Test
  @Requirement("ET-30")
  @DisplayName("Get Active Reservations by Charging Point ID")
  void testGetActiveReservationsByChargingPointId() {
    when(reservationRepository.findByChargingPointIdAndEndTimeAfter(
            eq(2L), any(LocalDateTime.class)))
        .thenReturn(List.of(reservation));

    List<ReservationResponseDTO> responses =
        reservationService.getActiveReservationsByChargingPointId(2L);

    assertEquals(1, responses.size());
    assertEquals(2L, responses.get(0).getChargingPoint().getId());
  }

  @Test
  @DisplayName("Fail to Create Reservation - End time before Start time")
  @Requirement("ET-30")
  void testCreateReservation_EndBeforeStart() {
    ReservationRequestDTO request =
        new ReservationRequestDTO(
            1L,
            2L,
            LocalDateTime.parse("2023-05-28T12:00:00"),
            LocalDateTime.parse("2023-05-28T10:00:00"));

    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class, () -> reservationService.createReservation(request));

    assertTrue(ex.getMessage().contains("Start time must be before end time"));
  }

  @Test
  @DisplayName("Fail to Create Reservation - Duration less than 15 minutes")
  @Requirement("ET-30")
  void testCreateReservation_TooShort() {
    ReservationRequestDTO request =
        new ReservationRequestDTO(
            1L,
            2L,
            LocalDateTime.parse("2023-05-28T10:00:00"),
            LocalDateTime.parse("2023-05-28T10:10:00"));

    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class, () -> reservationService.createReservation(request));

    assertTrue(ex.getMessage().contains("at least 15 minutes"));
  }

  @Test
  @DisplayName("Fail to Create Reservation - Duration exceeds 4 hours")
  @Requirement("ET-30")
  void testCreateReservation_TooLong() {
    ReservationRequestDTO request =
        new ReservationRequestDTO(
            1L,
            2L,
            LocalDateTime.parse("2023-05-28T10:00:00"),
            LocalDateTime.parse("2023-05-28T15:01:00"));

    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class, () -> reservationService.createReservation(request));

    assertTrue(ex.getMessage().contains("cannot exceed 4 hours"));
  }

  @Test
  @DisplayName("Fail to Create Reservation - Invalid User ID")
  @Requirement("ET-30")
  void testCreateReservation_InvalidUser() {
    when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

    ReservationRequestDTO request =
        new ReservationRequestDTO(
            999L,
            2L,
            LocalDateTime.parse("2023-05-28T10:00:00"),
            LocalDateTime.parse("2023-05-28T12:00:00"));

    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class, () -> reservationService.createReservation(request));

    assertTrue(ex.getMessage().contains("Invalid user ID"));
  }

  @Test
  @DisplayName("Fail to Create Reservation - Invalid Charging Point ID")
  @Requirement("ET-30")
  void testCreateReservation_InvalidChargingPoint() {
    when(chargingPointRepository.findById(anyLong())).thenReturn(Optional.empty());

    ReservationRequestDTO request =
        new ReservationRequestDTO(
            1L,
            999L,
            LocalDateTime.parse("2023-05-28T10:00:00"),
            LocalDateTime.parse("2023-05-28T12:00:00"));

    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class, () -> reservationService.createReservation(request));

    assertTrue(ex.getMessage().contains("Invalid charging point ID"));
  }
}
