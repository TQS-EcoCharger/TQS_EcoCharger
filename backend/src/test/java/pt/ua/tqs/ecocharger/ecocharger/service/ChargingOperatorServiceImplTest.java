package pt.ua.tqs.ecocharger.ecocharger.service;

import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Test;
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
    MockitoAnnotations.openMocks(this);
    chargingOperator = new ChargingOperator(1L, "testOperator", "password", "Test Operator", true);
    when(chargingOperatorRepository.findById(1L)).thenReturn(Optional.of(chargingOperator));
    when(chargingOperatorRepository.existsById(1L)).thenReturn(true);
  }

  @Test
  @DisplayName("Test getting Charging Operator by ID")
  public void testGetChargingOperatorById() {
    ChargingOperator result = chargingOperatorService.getChargingOperatorById(1L);
    assert result != null;
    assert result.getId().equals(1L);
    assert result.getName().equals("Test Operator");
  }

  @Test
  @DisplayName("Test finding out if id exists")
  public void testChargingOperatorExists() {
    boolean exists = chargingOperatorService.chargingOperatorExists(1L);
    assert exists;
    verify(chargingOperatorRepository, times(1)).existsById(1L);
  }
}
