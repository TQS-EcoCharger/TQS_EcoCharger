package pt.ua.tqs.ecocharger.ecocharger.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.getxray.xray.junit.customjunitxml.annotations.Requirement;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import pt.ua.tqs.ecocharger.ecocharger.config.SecurityDisableConfig;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingPoint;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingStation;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.ChargingPointService;

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

@WebMvcTest(ChargingPointController.class)
@Import(SecurityDisableConfig.class)
public class ChargingPointControllerTest {

  @Autowired private MockMvc mockMvc;

  @SuppressWarnings("removal")
  @MockBean
  private ChargingPointService chargingPointService;

  @Autowired private ObjectMapper objectMapper;

  @Test
  @DisplayName("Create a charging point")
  void testCreatePoint() throws Exception {
    ChargingStation station = new ChargingStation();
    station.setId(1L);
    ChargingPoint point = new ChargingPoint();
    point.setId(10L);
    point.setBrand("Tesla");

    Mockito.when(chargingPointService.createPoint(any(), any())).thenReturn(point);

    String json =
        """
        {
          "point": {
            "id": 10,
            "brand": "Tesla"
          },
          "station": {
            "id": 1
          }
        }
        """;
    mockMvc
        .perform(post("/api/v1/points").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(10));
  }

  @Test
  @DisplayName("Get all charging points")
  void testGetAllPoints() throws Exception {
    ChargingPoint point = new ChargingPoint();
    point.setId(1L);
    point.setBrand("Tesla");

    Mockito.when(chargingPointService.getAllPoints()).thenReturn(List.of(point));

    mockMvc
        .perform(get("/api/v1/points"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1L))
        .andExpect(jsonPath("$[0].brand").value("Tesla"));
  }

  @Test
  @DisplayName("Get available points for a station")
  void testGetAvailablePoints() throws Exception {
    ChargingStation station = new ChargingStation();
    station.setId(1L);

    ChargingPoint point = new ChargingPoint();
    point.setId(1L);
    point.setBrand("Tesla");

    Mockito.when(chargingPointService.getAvailablePoints(any())).thenReturn(List.of(point));

    mockMvc
        .perform(
            get("/api/v1/points/available")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(station)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].brand").value("Tesla"));
  }

  @Test
  @DisplayName("Get points by station ID")
  void testGetPointsByStationId() throws Exception {
    ChargingPoint point = new ChargingPoint();
    point.setId(1L);
    point.setBrand("Tesla");

    Mockito.when(chargingPointService.getPointsByStationId(1L)).thenReturn(List.of(point));

    mockMvc
        .perform(get("/api/v1/points/station/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1L));
  }

  @Test
  @DisplayName("Delete a point")
  void testDeletePoint() throws Exception {
    mockMvc.perform(delete("/api/v1/points/1")).andExpect(status().isNoContent());

    Mockito.verify(chargingPointService).deletePoint(1L);
  }

  @Test
  @Requirement("ET-43")
  @DisplayName("Get active session for a charging point")
  void testGetActiveSessionForPoint() throws Exception {
      var sessionDto = new pt.ua.tqs.ecocharger.ecocharger.dto.ActiveSessionDTO(
              5L, 1L, 3L, "Tesla Model S", 25, 85.0, 15.0, 12.5
      );

      Mockito.when(chargingPointService.getActiveSessionForPoint(1L)).thenReturn(sessionDto);

      mockMvc.perform(get("/api/v1/points/1/active-session"))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.sessionId").value(5L))
              .andExpect(jsonPath("$.carName").value("Tesla Model S"))
              .andExpect(jsonPath("$.batteryPercentage").value(85.0))
              .andExpect(jsonPath("$.energyDelivered").value(15.0))
              .andExpect(jsonPath("$.totalCost").value(12.5));
  }
}
