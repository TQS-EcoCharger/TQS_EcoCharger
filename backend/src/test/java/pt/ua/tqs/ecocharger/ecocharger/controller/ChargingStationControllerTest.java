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

import static org.mockito.ArgumentMatchers.*;
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
  @DisplayName("Successfully edit station")
  @Requirement("ET-22")
  void testEditStation() throws Exception {
    ChargingStation mockStation =
        new ChargingStation("Aveiro", "Rua A", 40.0, -8.0, "PT", "Portugal");
    mockStation.setId(1L);
    mockStation.setCityName("Updated City");

    Mockito.when(chargingStationService.updateStation(eq(1L), any(ChargingStation.class)))
        .thenReturn(mockStation);
    mockMvc
        .perform(
            put("/api/v1/chargingStations/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockStation)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.cityName").value("Updated City"));
  }

    @Test
    @DisplayName("Get all stations")
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

  @Test
    @DisplayName("Return 404 when deleting a non-existent station")
    @Requirement("ET-18")
    void testDeleteStationNotFound() throws Exception {
    Mockito.doThrow(new pt.ua.tqs.ecocharger.ecocharger.utils.NotFoundException("Station not found"))
        .when(chargingStationService)
        .deleteStation(999L);

    mockMvc.perform(delete("/api/v1/chargingStations/999"))
        .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Return 404 when updating a non-existent station")
    @Requirement("ET-22")
    void testUpdateStationNotFound() throws Exception {
    ChargingStation station =
        new ChargingStation("City", "Street", 0.0, 0.0, "CC", "Country");

    Mockito.when(chargingStationService.updateStation(eq(999L), any()))
        .thenThrow(new pt.ua.tqs.ecocharger.ecocharger.utils.NotFoundException(""));

    mockMvc
        .perform(put("/api/v1/chargingStations/999")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(station)))
        .andExpect(status().isNotFound());
    }


}
