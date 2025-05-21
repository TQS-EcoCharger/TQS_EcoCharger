package pt.ua.tqs.ecocharger.ecocharger.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingStation;
import pt.ua.tqs.ecocharger.ecocharger.repository.ChargingStationRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ChargingStationServiceImplTest {

    @Mock
    private ChargingStationRepository chargingStationRepository;

    @InjectMocks
    private ChargingStationServiceImpl chargingStationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateStation_NewStation() {
        ChargingStation station = new ChargingStation("Aveiro", "Rua A", 40.0, -8.0, "Rua A", "PT", "Portugal", "Electric");

        when(chargingStationRepository.findByCityName("Aveiro")).thenReturn(Optional.empty());
        when(chargingStationRepository.save(station)).thenReturn(station);

        ChargingStation result = chargingStationService.createStation(station);

        assertEquals("Aveiro", result.getCityName());
        verify(chargingStationRepository, times(1)).save(station);
    }

    @Test
    void testCreateStation_ExistingStation() {
        ChargingStation existingStation = new ChargingStation("Aveiro", "Rua A", 40.0, -8.0, "Rua A", "PT", "Portugal", "Electric");

        when(chargingStationRepository.findByCityName("Aveiro")).thenReturn(Optional.of(existingStation));

        ChargingStation result = chargingStationService.createStation(existingStation);

        assertEquals("Aveiro", result.getCityName());
        verify(chargingStationRepository, never()).save(any());
    }

    @Test
    void testGetAllStationsByCityName() {
        ChargingStation s1 = new ChargingStation("Aveiro", "Rua A", 40.0, -8.0, "Rua A", "PT", "Portugal", "Electric");
        ChargingStation s2 = new ChargingStation("Lisboa", "Rua B", 38.0, -9.0, "Rua B", "PT", "Portugal", "Hybrid");

        when(chargingStationRepository.findAll()).thenReturn(Arrays.asList(s1, s2));

        List<ChargingStation> result = chargingStationService.getAllStationsByCityName("Aveiro");

        assertEquals(1, result.size());
        assertEquals("Aveiro", result.get(0).getCityName());
    }

    @Test
    void testDeleteStation_Exists() {
        ChargingStation station = new ChargingStation("Porto", "Rua C", 41.0, -8.5, "Rua C", "PT", "Portugal", "Electric");
        station.setId(1L);

        when(chargingStationRepository.findById(1L)).thenReturn(Optional.of(station));

        chargingStationService.deleteStation(1L);

        verify(chargingStationRepository, times(1)).delete(station);
    }

    @Test
    void testDeleteStation_NotFound() {
        when(chargingStationRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            chargingStationService.deleteStation(2L);
        });

        assertEquals("Station not found", exception.getMessage());
        verify(chargingStationRepository, never()).delete(any());
    }

    @Test
    void testGetAllStations() {
        ChargingStation s1 = new ChargingStation("Aveiro", "Rua A", 40.0, -8.0, "Rua A", "PT", "Portugal", "Electric");
        ChargingStation s2 = new ChargingStation("Lisboa", "Rua B", 38.0, -9.0, "Rua B", "PT", "Portugal", "Hybrid");

        when(chargingStationRepository.findAll()).thenReturn(Arrays.asList(s1, s2));

        List<ChargingStation> result = chargingStationService.getAllStations();

        assertEquals(2, result.size());
    }
}
