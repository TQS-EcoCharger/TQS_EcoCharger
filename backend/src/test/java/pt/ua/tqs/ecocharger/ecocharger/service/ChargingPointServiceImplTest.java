package pt.ua.tqs.ecocharger.ecocharger.service;

import app.getxray.xray.junit.customjunitxml.annotations.Requirement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pt.ua.tqs.ecocharger.ecocharger.dto.ActiveSessionDTO;
import pt.ua.tqs.ecocharger.ecocharger.models.Car;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingPoint;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingSession;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingStation;
import pt.ua.tqs.ecocharger.ecocharger.repository.ChargingPointRepository;
import pt.ua.tqs.ecocharger.ecocharger.utils.NotFoundException;
import pt.ua.tqs.ecocharger.ecocharger.repository.ChargingSessionRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ChargingPointServiceImplTest {

  @Mock private ChargingPointRepository chargingPointRepository;
  @Mock private ChargingSessionRepository chargingSessionRepository;

  @InjectMocks private ChargingPointServiceImpl chargingPointService;

  private ChargingStation station;
  private ChargingPoint point;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    station = new ChargingStation("Aveiro", "Rua A", 40.0, -8.0, "PT", "Portugal");
    station.setId(1L);

    point = new ChargingPoint();
    point.setId(1L);
    point.setBrand("Tesla");
    point.setAvailable(true);
  }

  @Test
  @DisplayName("Should create new charging point when it doesn't exist")
  @Requirement("ET-18")
  void testCreateChargingPoint_NewChargingPoint() {
    when(chargingPointRepository.findByChargingStationId(station.getId()))
        .thenReturn(Optional.of(new ArrayList<>()));
    when(chargingPointRepository.save(point)).thenReturn(point);

    ChargingPoint result = chargingPointService.createPoint(point, station);

    assertEquals(point.getId(), result.getId());
    assertEquals(station, result.getChargingStation());
    verify(chargingPointRepository).save(point);
  }

  @Test
  @DisplayName("Should return existing charging point if ID matches")
  @Requirement("ET-18")
  void testCreateChargingPoint_AlreadyExists() {
    List<ChargingPoint> existingPoints = List.of(point);
    when(chargingPointRepository.findByChargingStationId(station.getId()))
        .thenReturn(Optional.of(existingPoints));

    ChargingPoint result = chargingPointService.createPoint(point, station);

    assertEquals(point.getId(), result.getId());
    verify(chargingPointRepository, never()).save(any());
  }

  @Test
  @DisplayName("Should return all non-null ID charging points")
  @Requirement("ET-18")
  void testGetAllPoints() {
    ChargingPoint p2 = new ChargingPoint();
    p2.setId(2L);
    when(chargingPointRepository.findAll()).thenReturn(Arrays.asList(point, p2));

    List<ChargingPoint> result = chargingPointService.getAllPoints();

    assertEquals(2, result.size());
  }

  @Test
  @DisplayName("Should return available points for a station")
  @Requirement("ET-18")
  void testGetAvailablePoints() {
    List<ChargingPoint> availablePoints = List.of(point);
    when(chargingPointRepository.findAvailablePointsByChargingStation(station))
        .thenReturn(Optional.of(availablePoints));

    List<ChargingPoint> result = chargingPointService.getAvailablePoints(station);

    assertEquals(1, result.size());
    assertTrue(result.get(0).isAvailable());
  }

  @Test
  @DisplayName("Should successfully update an existing charging point")
  @Requirement("ET-22")
  void testUpdatePoint_Exists() {
    ChargingPoint updatedPoint = new ChargingPoint();
    updatedPoint.setId(1L);
    updatedPoint.setBrand("Updated Brand");

    when(chargingPointRepository.findById(1L)).thenReturn(Optional.of(point));
    when(chargingPointRepository.save(any(ChargingPoint.class))).thenReturn(updatedPoint);

    ChargingPoint result = chargingPointService.updatePoint(1L, updatedPoint);
    assertEquals("Updated Brand", result.getBrand());
    verify(chargingPointRepository).save(updatedPoint);
  }

  @Test
  @DisplayName("Should throw exception when trying to update non-existent charging point")
  @Requirement("ET-22")
  void testUpdatePoint_NotFound() {
    ChargingPoint updatedPoint = new ChargingPoint();
    updatedPoint.setId(2L);

    when(chargingPointRepository.findById(2L)).thenReturn(Optional.empty());
    Exception exception =
        assertThrows(
            NotFoundException.class, () -> chargingPointService.updatePoint(2L, updatedPoint));
    assertEquals("Point not found", exception.getMessage());
    verify(chargingPointRepository, never()).save(any());
  }

  @Test
  @DisplayName("Should throw exception when no available points found")
  @Requirement("ET-18")
  void testGetAvailablePoints_Empty() {
    when(chargingPointRepository.findAvailablePointsByChargingStation(station))
        .thenReturn(Optional.empty());

    Exception exception =
        assertThrows(
            RuntimeException.class, () -> chargingPointService.getAvailablePoints(station));

    assertEquals("No available points found", exception.getMessage());
  }

  @Test
  @DisplayName("Should delete charging point if exists")
  @Requirement("ET-18")
  void testDeletePoint_Exists() {
    when(chargingPointRepository.findById(1L)).thenReturn(Optional.of(point));

    chargingPointService.deletePoint(1L);

    verify(chargingPointRepository).delete(point);
  }

  @Test
  @DisplayName("Should throw exception when trying to delete non-existent point")
  @Requirement("ET-18")
  void testDeletePoint_NotFound() {
    when(chargingPointRepository.findById(2L)).thenReturn(Optional.empty());

    Exception exception =
        assertThrows(RuntimeException.class, () -> chargingPointService.deletePoint(2L));

    assertEquals("Point not found", exception.getMessage());
    verify(chargingPointRepository, never()).delete(any());
  }

  @Test
  @DisplayName("Should return all points by station ID")
  @Requirement("ET-18")
  void testGetPointsByStationId() {
    List<ChargingPoint> points = List.of(point);
    when(chargingPointRepository.findByChargingStationId(1L)).thenReturn(Optional.of(points));

    List<ChargingPoint> result = chargingPointService.getPointsByStationId(1L);

    assertEquals(1, result.size());
  }

  @Test
  @DisplayName("Should throw exception when no points found by station ID")
  @Requirement("ET-18")
  void testGetPointsByStationId_NotFound() {
    when(chargingPointRepository.findByChargingStationId(1L)).thenReturn(Optional.empty());

    Exception exception =
        assertThrows(RuntimeException.class, () -> chargingPointService.getPointsByStationId(1L));

    assertEquals("No points found for this station", exception.getMessage());
  }

  @Test
  @DisplayName("Update point with valid information")
  @Requirement("ET-22")
  void testUpdatePoint_Valid() {
    ChargingPoint updatedPoint = new ChargingPoint();
    updatedPoint.setId(1L);
    updatedPoint.setBrand("Updated Brand");
    updatedPoint.setAvailable(false);

    when(chargingPointRepository.findById(1L)).thenReturn(Optional.of(point));
    when(chargingPointRepository.save(any(ChargingPoint.class))).thenReturn(updatedPoint);

    ChargingPoint result = chargingPointService.updatePoint(1L, updatedPoint);

    assertEquals("Updated Brand", result.getBrand());
    assertFalse(result.isAvailable());
    verify(chargingPointRepository).save(updatedPoint);
  }

  @Test
  @Requirement("ET-43")
  @DisplayName("Should return active session DTO for a charging point")
  void testGetActiveSessionForPoint_Success() {
    ChargingSession session = new ChargingSession();
    session.setId(100L);
    session.setStartTime(LocalDateTime.now().minusMinutes(30));
    session.setInitialBatteryLevel(10.0);
    session.setCar(new Car());
    session.getCar().setId(5L);
    session.getCar().setBatteryCapacity(50.0);
    session.getCar().setName("Model Y");

    point.setId(1L);
    point.setChargingRateKWhPerMinute(1.0);
    point.setPricePerKWh(0.2);
    point.setPricePerMinute(0.1);

    when(chargingPointRepository.findById(1L)).thenReturn(Optional.of(point));
    when(chargingSessionRepository.findByChargingPointAndEndTimeIsNull(point))
        .thenReturn(Optional.of(session));

    ActiveSessionDTO dto = chargingPointService.getActiveSessionForPoint(1L);

    assertNotNull(dto);
    assertEquals(100L, dto.getSessionId());
    assertEquals(5L, dto.getCarId());
    assertEquals("Model Y", dto.getCarName());
    assertTrue(dto.getBatteryPercentage() > 0);
    assertTrue(dto.getEnergyDelivered() > 0);
    assertTrue(dto.getTotalCost() > 0);
  }

  @Test
  @Requirement("ET-43")
  @DisplayName("Should throw when no active session on the point")
  void testGetActiveSessionForPoint_NoSession() {
    when(chargingPointRepository.findById(1L)).thenReturn(Optional.of(point));
    when(chargingSessionRepository.findByChargingPointAndEndTimeIsNull(point))
        .thenReturn(Optional.empty());

    Exception ex =
        assertThrows(
            IllegalStateException.class, () -> chargingPointService.getActiveSessionForPoint(1L));

    assertEquals("No active session on this point", ex.getMessage());
  }

  @Test
  @Requirement("ET-43")
  @DisplayName("Should throw when charging point does not exist")
  void testGetActiveSessionForPoint_PointNotFound() {
    when(chargingPointRepository.findById(99L)).thenReturn(Optional.empty());

    Exception ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> chargingPointService.getActiveSessionForPoint(99L));

    assertEquals("Charging point not found", ex.getMessage());
  }
}
