package pt.ua.tqs.ecocharger.ecocharger.service;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import pt.ua.tqs.ecocharger.ecocharger.repository.ChargingOperatorRepository;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingOperator;

@ExtendWith(MockitoExtension.class)
public class ChargingOperatorServiceImplTest {

  @Mock private ChargingOperatorRepository chargingOperatorRepository;

  @InjectMocks private ChargingOperatorServiceImpl chargingOperatorService;

  ChargingOperator chargingOperator;

    @BeforeEach
    public void setUp() {
        chargingOperator = new ChargingOperator(1L, "testOperator", "password", "Test Operator", true);
    }

    @Test
    @DisplayName("Test getting Charging Operator by ID")
    public void testGetChargingOperatorById() {
        when(chargingOperatorRepository.findById(1L)).thenReturn(Optional.of(chargingOperator));
        ChargingOperator result = chargingOperatorService.getChargingOperatorById(1L);
        assertNotEquals(null, result);
        assertEquals(1L, result.getId());
        assertEquals("Test Operator", result.getName());
    }

    @Test
    @DisplayName("Test finding out if id exists")
    public void testChargingOperatorExists() {
        when(chargingOperatorRepository.existsById(1L)).thenReturn(true);
        boolean exists = chargingOperatorService.chargingOperatorExists(1L);
        assertTrue(exists);
        verify(chargingOperatorRepository, times(1)).existsById(1L);
    }
    
}
