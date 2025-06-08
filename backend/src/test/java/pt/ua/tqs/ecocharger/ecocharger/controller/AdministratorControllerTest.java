package pt.ua.tqs.ecocharger.ecocharger.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import app.getxray.xray.junit.customjunitxml.annotations.Requirement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pt.ua.tqs.ecocharger.ecocharger.config.SecurityDisableConfig;
import pt.ua.tqs.ecocharger.ecocharger.models.Administrator;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingPoint;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingStation;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.AdministratorService;

@WebMvcTest(AdministratorController.class)
@Import(SecurityDisableConfig.class)
class AdministratorControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private AdministratorService administratorService;

  @TestConfiguration
  static class MockServiceConfig {
    @Bean
    @Primary
    public AdministratorService administratorService() {
      return mock(AdministratorService.class);
    }
  }

  @Test
  @DisplayName("Create a new administrator")
  @Requirement("ET-20")
  void testCreateAdministrator() throws Exception {
    Administrator mockAdmin = new Administrator(1L, "tomas@gmail.com", "pass1", "Tomas", true);

    when(administratorService.createAdministrator("tomas@gmail.com", "pass1", "Tomas"))
        .thenReturn(mockAdmin);

    String requestBody =
        """
            {
                "type": "administrators",
                "email": "tomas@gmail.com",
                "password": "pass1",
                "name": "Tomas"
            }
        """;

    mockMvc
        .perform(post("/api/v1/admin").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value("tomas@gmail.com"))
        .andExpect(jsonPath("$.name").value("Tomas"))
        .andExpect(jsonPath("$.enabled").value(true))
        .andExpect(jsonPath("$.password").value("pass1")); // Avoid returning passwords in real apps
  }

  @Test
  @DisplayName("Update a charging station")
  @Requirement("ET-20")
  void testUpdateChargingStation() throws Exception {
    ChargingStation updatedStation =
        new ChargingStation("Lisboa", "Rua A", 38.7169, -9.1399, "PT", "Portugal");
    updatedStation.setId(1L);

    when(administratorService.updateChargingStation(any(ChargingStation.class)))
        .thenReturn(updatedStation);

    String requestBody =
        """
        {
            "cityName": "Lisboa",
            "address": "Rua A",
            "latitude": 38.7169,
            "longitude": -9.1399,
            "countryCode": "PT",
            "country": "Portugal"
        }
        """;

    mockMvc
        .perform(
            put("/api/v1/admin/stations/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.cityName").value("Lisboa"))
        .andExpect(jsonPath("$.address").value("Rua A"))
        .andExpect(jsonPath("$.latitude").value(38.7169))
        .andExpect(jsonPath("$.longitude").value(-9.1399))
        .andExpect(jsonPath("$.countryCode").value("PT"))
        .andExpect(jsonPath("$.country").value("Portugal"));
  }

  @Test
  @DisplayName("Delete a charging station")
  @Requirement("ET-20")
  void testDeleteChargingStation() throws Exception {
    ChargingStation station =
        new ChargingStation("Porto", "Rua B", 41.1496, -8.6109, "PT", "Portugal");
    station.setId(1L);
    when(administratorService.deleteChargingStation(1L)).thenReturn(station);
    mockMvc
        .perform(delete("/api/v1/admin/stations/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.cityName").value("Porto"))
        .andExpect(jsonPath("$.address").value("Rua B"))
        .andExpect(jsonPath("$.latitude").value(41.1496))
        .andExpect(jsonPath("$.longitude").value(-8.6109))
        .andExpect(jsonPath("$.countryCode").value("PT"))
        .andExpect(jsonPath("$.country").value("Portugal"));
    verify(administratorService, times(1)).deleteChargingStation(1L);
  }

  @Test
  @DisplayName("Should update a charging point")
  @Requirement("ET-20")
  void testUpdateChargingPoint() throws Exception {
    ChargingPoint point = new ChargingPoint();
    point.setId(1L);
    point.setAvailable(true);
    point.setBrand("Tesla");
    point.setPricePerKWh(0.25);
    point.setPricePerMinute(0.10);

    when(administratorService.updateChargingPoint(any(ChargingPoint.class), eq(1L)))
        .thenReturn(point);

    String requestBody =
        """
        {
          "available": true,
          "brand": "Tesla",
          "pricePerKwh": 0.25,
          "pricePerMinute": 0.10
        }
        """;

    mockMvc
        .perform(
            put("/api/v1/admin/stations/1/points/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.brand").value("Tesla"))
        .andExpect(jsonPath("$.available").value(true));
  }
}
