package pt.ua.tqs.ecocharger.ecocharger.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.getxray.xray.junit.customjunitxml.annotations.Requirement;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import pt.ua.tqs.ecocharger.ecocharger.config.SecurityDisableConfig;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingStation;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.ChargingStationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChargingStationController.class)
@Import(SecurityDisableConfig.class)
class ChargingStationControllerTest {

  @Autowired private MockMvc mockMvc;

  @SuppressWarnings("removal")
  @MockBean
  private ChargingStationService chargingStationService;

  @Autowired private ObjectMapper objectMapper;

  @Test
  @DisplayName("Create station")
  @Requirement("ET-18")
  void testCreateStation() throws Exception {
    ChargingStation mockStation =
        new ChargingStation("Aveiro", "Rua A", 40.0, -8.0, "PT", "Portugal");
    mockStation.setId(1L);

    Mockito.when(chargingStationService.createStation(any(ChargingStation.class)))
        .thenReturn(mockStation);

    mockMvc
        .perform(
            post("/api/v1/chargingStations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockStation)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.cityName").value("Aveiro"));
  }

  @Test
  @DisplayName("Return 404 when station not found")
  @Requirement("ET-18")
  void testGetAllStations() throws Exception {
    ChargingStation mockStation =
        new ChargingStation("Aveiro", "Rua A", 40.0, -8.0, "PT", "Portugal");
    mockStation.setId(1L);

    Mockito.when(chargingStationService.getAllStations()).thenReturn(List.of(mockStation));

    mockMvc
        .perform(get("/api/v1/chargingStations"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].cityName").value("Aveiro"));
  }

  @Test
  @DisplayName("Return 404 when station not found")
  @Requirement("ET-18")
  void testGetStationsByCity() throws Exception {
    ChargingStation mockStation =
        new ChargingStation("Aveiro", "Rua A", 40.0, -8.0, "PT", "Portugal");
    mockStation.setId(1L);

    Mockito.when(chargingStationService.getAllStationsByCityName("Aveiro"))
        .thenReturn(List.of(mockStation));

    mockMvc
        .perform(get("/api/v1/chargingStations/city/Aveiro"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].cityName").value("Aveiro"));
  }

  @Test
  @DisplayName("Delete station")
  @Requirement("ET-18")
  void testDeleteStation() throws Exception {
    mockMvc.perform(delete("/api/v1/chargingStations/1")).andExpect(status().isNoContent());

    Mockito.verify(chargingStationService).deleteStation(1L);
  }
}
