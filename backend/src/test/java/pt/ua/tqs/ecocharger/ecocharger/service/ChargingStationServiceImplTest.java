package pt.ua.tqs.ecocharger.ecocharger.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import app.getxray.xray.junit.customjunitxml.annotations.Requirement;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingStation;
import pt.ua.tqs.ecocharger.ecocharger.repository.ChargingStationRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ChargingStationServiceImplTest {

  @Mock private ChargingStationRepository chargingStationRepository;

  @InjectMocks private ChargingStationServiceImpl chargingStationService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @Requirement("ET-18")
  void testCreateStation_NewStation() {
    ChargingStation station = new ChargingStation("Aveiro", "Rua A", 40.0, -8.0, "PT", "Portugal");

    when(chargingStationRepository.findByCityName("Aveiro")).thenReturn(Optional.empty());
    when(chargingStationRepository.save(station)).thenReturn(station);

    ChargingStation result = chargingStationService.createStation(station);

    assertEquals("Aveiro", result.getCityName());
    verify(chargingStationRepository, times(1)).save(station);
  }

  @Test
  @Requirement("ET-18")
  void testCreateStation_ExistingStation() {
    ChargingStation existingStation =
        new ChargingStation("Aveiro", "Rua A", 40.0, -8.0, "PT", "Portugal");

    when(chargingStationRepository.findByCityName("Aveiro"))
        .thenReturn(Optional.of(Arrays.asList(existingStation)));

    ChargingStation result = chargingStationService.createStation(existingStation);

    assertEquals("Aveiro", result.getCityName());
    verify(chargingStationRepository, never()).save(any());
  }

  @Test
  @Requirement("ET-18")
  void testGetAllStationsByCityName() {
    ChargingStation s1 = new ChargingStation("Aveiro", "Rua A", 40.0, -8.0, "PT", "Portugal");
    ChargingStation s2 = new ChargingStation("Lisboa", "Rua B", 38.0, -9.0, "PT", "Portugal");

    when(chargingStationRepository.findAll()).thenReturn(Arrays.asList(s1, s2));

    List<ChargingStation> result = chargingStationService.getAllStationsByCityName("Aveiro");

    assertEquals(1, result.size());
    assertEquals("Aveiro", result.get(0).getCityName());
  }

  @Test
  @Requirement("ET-18")
  void testDeleteStation_Exists() {
    ChargingStation station = new ChargingStation("Porto", "Rua C", 41.0, -8.5, "PT", "Portugal");
    station.setId(1L);

    when(chargingStationRepository.findById(1L)).thenReturn(Optional.of(station));

    chargingStationService.deleteStation(1L);

    verify(chargingStationRepository, times(1)).delete(station);
  }

  @Test
  @Requirement("ET-18")
  void testDeleteStation_NotFound() {
    when(chargingStationRepository.findById(2L)).thenReturn(Optional.empty());

    Exception exception =
        assertThrows(
            RuntimeException.class,
            () -> {
              chargingStationService.deleteStation(2L);
            });

    assertEquals("Station not found", exception.getMessage());
    verify(chargingStationRepository, never()).delete(any());
  }

  @Test
  @Requirement("ET-18")
  void testGetAllStations() {
    ChargingStation s1 = new ChargingStation("Aveiro", "Rua A", 40.0, -8.0, "PT", "Portugal");
    ChargingStation s2 = new ChargingStation("Lisboa", "Rua B", 38.0, -9.0, "PT", "Portugal");

    when(chargingStationRepository.findAll()).thenReturn(Arrays.asList(s1, s2));

    List<ChargingStation> result = chargingStationService.getAllStations();

    assertEquals(2, result.size());
  }

  @Test
  @Requirement("ET-22")
  void testUpdateStation_Exists() {
    ChargingStation existingStation = new ChargingStation("Porto", "Rua C", 41.0, -8.5, "PT", "Portugal");
    existingStation.setId(1L);
    ChargingStation updatedStation = new ChargingStation("Porto", "Rua D", 41.0, -8.5, "PT", "Portugal");
    updatedStation.setId(1L);

    when(chargingStationRepository.findById(1L)).thenReturn(Optional.of(existingStation));
    when(chargingStationRepository.save(any(ChargingStation.class))).thenReturn(updatedStation);
    ChargingStation result = chargingStationService.updateStation(1L, updatedStation);
    assertEquals("Porto", result.getCityName());
    assertEquals("Rua D", result.getAddress());
    verify(chargingStationRepository, times(1)).save(any(ChargingStation.class));
  }
}
