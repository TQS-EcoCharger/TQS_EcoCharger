package pt.ua.tqs.ecocharger.ecocharger.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import app.getxray.xray.junit.customjunitxml.annotations.Requirement;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pt.ua.tqs.ecocharger.ecocharger.models.Administrator;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingPoint;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingStation;
import pt.ua.tqs.ecocharger.ecocharger.models.Connectors;
import pt.ua.tqs.ecocharger.ecocharger.repository.AdministratorRepository;
import pt.ua.tqs.ecocharger.ecocharger.repository.ChargingPointRepository;
import pt.ua.tqs.ecocharger.ecocharger.repository.ChargingStationRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AdministratorServiceImplTest {

  @Mock private AdministratorRepository administratorRepository;

  @Mock private ChargingStationRepository chargingStationRepository;

  @Mock private ChargingPointRepository chargingPointRepository;

  @InjectMocks private AdministratorServiceImpl administratorService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @Requirement("ET-20")
  @DisplayName("Create new administrator successfully")
  void testCreateAdministratorSuccess() {
    String email = "admin@example.com";
    String password = "password123";
    String name = "Admin";

    when(administratorRepository.findByEmail(email)).thenReturn(Optional.empty());

    Administrator savedAdmin = new Administrator();
    savedAdmin.setEmail(email);
    savedAdmin.setPassword(password);
    savedAdmin.setName(name);
    when(administratorRepository.save(any(Administrator.class))).thenReturn(savedAdmin);

    Administrator result = administratorService.createAdministrator(email, password, name);

    assertNotNull(result);
    assertEquals(email, result.getEmail());
    verify(administratorRepository, times(1)).save(any(Administrator.class));
  }

  @Test
  @Requirement("ET-20")
  @DisplayName("Fail to create administrator due to existing email")
  void testCreateAdministratorAlreadyExists() {
    String email = "admin@example.com";
    when(administratorRepository.findByEmail(email)).thenReturn(Optional.of(new Administrator()));

    RuntimeException ex =
        assertThrows(
            RuntimeException.class,
            () -> administratorService.createAdministrator(email, "123", "Admin"));

    assertEquals("Administrator with this email already exists", ex.getMessage());
  }

  @Test
  @Requirement("ET-20")
  @DisplayName("Successfully update a charging station")
  void testUpdateChargingStationSuccess() {
    ChargingStation existing = new ChargingStation();
    existing.setId(1L);
    existing.setCityName("OldCity");

    ChargingStation updated = new ChargingStation();
    updated.setId(1L);
    updated.setCityName("NewCity");

    when(chargingStationRepository.findById(1L)).thenReturn(Optional.of(existing));
    when(chargingStationRepository.save(any(ChargingStation.class))).thenReturn(updated);

    ChargingStation result = administratorService.updateChargingStation(updated);

    assertEquals("NewCity", result.getCityName());
    verify(chargingStationRepository).save(existing);
  }

  @Test
  @Requirement("ET-20")
  @DisplayName("Fail to update a non-existent charging station")
  void testUpdateChargingStationNotFound() {
    when(chargingStationRepository.findById(999L)).thenReturn(Optional.empty());

    ChargingStation station = new ChargingStation();
    station.setId(999L);

    RuntimeException ex =
        assertThrows(
            RuntimeException.class, () -> administratorService.updateChargingStation(station));

    assertEquals("Charging Station not found", ex.getMessage());
  }

  @Test
  @Requirement("ET-20")
  @DisplayName("Successfully delete a charging station")
  void testDeleteChargingStationSuccess() {
    Administrator admin = new Administrator();
    admin.setId(1L);

    ChargingStation station = new ChargingStation();
    station.setId(1L);
    station.setAddedBy(admin);
    admin.getAddedStations().add(station);

    when(chargingStationRepository.findById(1L)).thenReturn(Optional.of(station));
    when(administratorRepository.findById(1L)).thenReturn(Optional.of(admin));

    ChargingStation result = administratorService.deleteChargingStation(1L);

    assertEquals(1L, result.getId());
    verify(chargingStationRepository).delete(station);
    verify(administratorRepository).save(admin);
  }

  @Test
  @Requirement("ET-20")
  @DisplayName("Fail to delete charging station: not found")
  void testDeleteChargingStationNotFound() {
    when(chargingStationRepository.findById(1L)).thenReturn(Optional.empty());

    RuntimeException ex =
        assertThrows(RuntimeException.class, () -> administratorService.deleteChargingStation(1L));

    assertEquals("Charging Station not found", ex.getMessage());
  }

  @Test
  @Requirement("ET-20")
  @DisplayName("Fail to delete charging station: admin not found")
  void testDeleteChargingStationAdminNotFound() {
    Administrator admin = new Administrator();
    admin.setId(1L);

    ChargingStation station = new ChargingStation();
    station.setId(1L);
    station.setAddedBy(admin);

    when(chargingStationRepository.findById(1L)).thenReturn(Optional.of(station));
    when(administratorRepository.findById(1L)).thenReturn(Optional.empty());

    RuntimeException ex =
        assertThrows(RuntimeException.class, () -> administratorService.deleteChargingStation(1L));

    assertEquals("Administrator not found", ex.getMessage());
  }

  @Test
  @Requirement("ET-20")
  @DisplayName("Successfully update a charging point")
  void testUpdateChargingPointSuccess() {
    ChargingPoint existingPoint = new ChargingPoint();
    existingPoint.setId(1L);
    existingPoint.setBrand("OldBrand");

    ChargingPoint updateData = new ChargingPoint();
    updateData.setBrand("NewBrand");
    updateData.setAvailable(true);
    updateData.setPricePerKWh(0.3);
    updateData.setPricePerMinute(0.1);
    updateData.setConnectors(List.of(new Connectors())); // Adicionar conector válido

    when(chargingPointRepository.findById(1L)).thenReturn(Optional.of(existingPoint));
    when(chargingPointRepository.save(any(ChargingPoint.class))).thenReturn(existingPoint);

    ChargingPoint result = administratorService.updateChargingPoint(updateData, 1L);

    assertEquals("NewBrand", result.getBrand());
    verify(chargingPointRepository).save(existingPoint);
  }

  @Test
  @Requirement("ET-20")
  @DisplayName("Fail to update non-existent charging point")
  void testUpdateChargingPointNotFound() {
    ChargingPoint updateData = new ChargingPoint();
    updateData.setId(999L);

    when(chargingPointRepository.findById(999L)).thenReturn(Optional.empty());

    RuntimeException ex =
        assertThrows(
            RuntimeException.class,
            () -> administratorService.updateChargingPoint(updateData, 999L));

    assertEquals("Charging Point not found", ex.getMessage());
  }
}
