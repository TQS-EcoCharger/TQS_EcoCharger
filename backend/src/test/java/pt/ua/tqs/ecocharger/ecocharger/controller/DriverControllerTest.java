package pt.ua.tqs.ecocharger.ecocharger.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import app.getxray.xray.junit.customjunitxml.annotations.Requirement;
import pt.ua.tqs.ecocharger.ecocharger.config.SecurityDisableConfig;
import pt.ua.tqs.ecocharger.ecocharger.dto.BalanceTopUpRequest;
import pt.ua.tqs.ecocharger.ecocharger.models.Car;
import pt.ua.tqs.ecocharger.ecocharger.models.Driver;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.DriverService;
import pt.ua.tqs.ecocharger.ecocharger.utils.NotFoundException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.hasSize;

import org.junit.jupiter.api.BeforeEach;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(DriverController.class)
@Import(SecurityDisableConfig.class)
class DriverControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired @MockitoBean private DriverService driverService;

  private Driver driver1;
  private Driver driver2;
  private Driver driver3;
  private Driver driver4;
  private Driver driver5;

  @BeforeEach
  void setUp() {
    driver1 = new Driver(1L, "johndoe@example.com", "password", "John Doe", true);
    driver2 = new Driver(2L, "katherinedoe@example.com", "password2", "Katherine Doe", true);
    driver3 = new Driver(3L, "paulbrook@example.com", "password3", "Paul Brook", true);
    driver4 = new Driver(4L, "viktorpineapple@example.com", "password4", "Viktor Pineapple", true);
    driver5 = new Driver(5L, "sergeyaleixov@example.com", "password5", "Sergey Aleixov", true);

    when(driverService.getAllDrivers())
        .thenReturn(List.of(driver1, driver2, driver3, driver4, driver5));
    when(driverService.getDriverById(1L)).thenReturn(driver1);
    when(driverService.getDriverById(2L)).thenReturn(driver2);
    when(driverService.getDriverById(3L)).thenReturn(driver3);
    when(driverService.getDriverById(4L)).thenReturn(driver4);
    when(driverService.getDriverById(5L)).thenReturn(driver5);
    when(driverService.getDriverById(6L)).thenThrow(new NotFoundException("Driver not found"));
  }

  @Test
  @DisplayName("Getting all drivers returns 200 OK and list in JSON")
  void testGetAllDrivers() throws Exception {
    mockMvc
        .perform(get("/api/v1/driver/"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(5)))
        .andExpect(jsonPath("$[0].name").value("John Doe"))
        .andExpect(jsonPath("$[1].name").value("Katherine Doe"))
        .andExpect(jsonPath("$[2].name").value("Paul Brook"))
        .andExpect(jsonPath("$[3].name").value("Viktor Pineapple"))
        .andExpect(jsonPath("$[4].name").value("Sergey Aleixov"));
  }

  @Test
  @DisplayName("Getting driver by ID returns 200 OK and driver in JSON if it exists")
  void testGetDriverById() throws Exception {
    mockMvc
        .perform(get("/api/v1/driver/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("John Doe"));
  }

  @Test
  @DisplayName("Getting driver by ID returns 404 NOT FOUND if it does not exist")
  void testGetDriverByIdNotFound() throws Exception {
    mockMvc
        .perform(get("/api/v1/driver/6"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$").value("Driver not found"));
  }

  @Test
  @DisplayName("Updating driver returns 200 OK and updated driver in JSON if it exists")
  void testUpdateDriver() throws Exception {
    Driver updatedDriver =
        new Driver(null, "johnfantastic@example.com", "newpassword", "John Fantastic", true);
    driver1.setEmail(updatedDriver.getEmail());
    driver1.setPassword(updatedDriver.getPassword());
    driver1.setName(updatedDriver.getName());
    driver1.setEnabled(updatedDriver.isEnabled());

    when(driverService.updateDriver(eq(1L), any(Driver.class))).thenReturn(driver1);

    mockMvc
        .perform(
            put("/api/v1/driver/1")
                .contentType("application/json")
                .content(
                    """
                    {
                      "email":"johnfantastic@example.com",
                      "password":"newpassword",
                      "name":"John Fantastic",
                      "enabled": true,
                      "type": "drivers"
                    }
                    """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value(driver1.getEmail()));
  }

  @Test
  @DisplayName("Updating driver returns 404 NOT FOUND if it does not exist")
  void testUpdateDriverNotFound() throws Exception {
    when(driverService.updateDriver(eq(6L), any(Driver.class)))
        .thenThrow(new NotFoundException("Driver not found"));

    mockMvc
        .perform(
            put("/api/v1/driver/6")
                .contentType("application/json")
                .content(
                    """
                    {
                      "email":"johnfantastic@example.com",
                      "password":"newpassword",
                      "name":"John Fantastic",
                      "enabled": true,
                      "type": "drivers"
                    }
                    """))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$").value("Driver not found"));
  }

  @Test
  @DisplayName("Adding car to driver returns 200 OK and updated driver in JSON if it exists")
  void testAddCarToDriver() throws Exception {
    Car car =
        new Car(1L, "Tesla Model S", "Tesla", "Model S", 2022, "ABC123", 100.0, 50.0, 0.0, 0.0);
    driver1.addCar(car);
    when(driverService.addCarToDriver(eq(1L), any(Car.class))).thenReturn(driver1);
    mockMvc
        .perform(
            patch("/api/v1/driver/1/cars/")
                .contentType("application/json")
                .content(
                    "{\"id\":1, \"make\":\"Tesla\", \"model\":\"Model S\", \"year\":2022,"
                        + " \"licensePlate\":\"ABC123\", \"batteryCapacity\":100.0,"
                        + " \"currentCharge\":50.0, \"kilometers\":0.0, \"consumption\":0.0}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.cars", hasSize(1)));
  }

  @Test
  @DisplayName("Adding car to driver returns 404 NOT FOUND if driver does not exist")
  void testAddCarToDriverNotFound() throws Exception {
    when(driverService.addCarToDriver(eq(6L), any(Car.class)))
        .thenThrow(new NotFoundException("Driver not found"));
    mockMvc
        .perform(
            patch("/api/v1/driver/6/cars/")
                .contentType("application/json")
                .content(
                    "{\"id\":1, \"make\":\"Tesla\", \"model\":\"Model S\", \"year\":2022,"
                        + " \"licensePlate\":\"ABC123\", \"batteryCapacity\":100.0,"
                        + " \"currentCharge\":50.0, \"mileage\":0.0, \"consumption\":0.0}"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$").value("Driver not found"));
  }

  @Test
  @DisplayName("Removing car from driver returns 200 OK and updated driver in JSON if it exists")
  void testRemoveCarFromDriver() throws Exception {
    when(driverService.removeCarFromDriver(1L, 2L)).thenReturn(driver1);

    mockMvc
        .perform(delete("/api/v1/driver/1/cars/2"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value(driver1.getEmail()));
  }

  @Test
  @DisplayName("Removing car from driver returns 404 NOT FOUND if driver does not exist")
  void testRemoveCarFromDriverNotFound() throws Exception {
    when(driverService.removeCarFromDriver(6L, 2L))
        .thenThrow(new NotFoundException("Driver not found"));
    mockMvc
        .perform(
            delete("/api/v1/driver/6/cars/2")
                .contentType("application/json")
                .content("{\"carId\":2}"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$").value("Driver not found"));
  }

  @Test
  @DisplayName("Removing car from driver returns 404 NOT FOUND if car does not exist")
  void testRemoveCarFromDriverCarNotFound() throws Exception {
    when(driverService.removeCarFromDriver(1L, 6L))
        .thenThrow(new NotFoundException("Car not found"));
    mockMvc
        .perform(
            delete("/api/v1/driver/1/cars/6")
                .contentType("application/json")
                .content("{\"carId\":6}"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$").value("Car not found"));
  }

  @Test
  @DisplayName("Deleting driver returns 200 OK if it exists")
  void testDeleteDriver() throws Exception {
    mockMvc.perform(delete("/api/v1/driver/1")).andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("Deleting driver returns 404 NOT FOUND if it does not exist")
  void testDeleteDriverNotFound() throws Exception {
    doThrow(new NotFoundException("Driver not found")).when(driverService).deleteDriver(6L);
    mockMvc
        .perform(delete("/api/v1/driver/6"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$").value("Driver not found"));
  }

  @Test
  @Requirement("ET-42")
  @DisplayName("Top-up creates Stripe session and returns URL")
  void testTopUpBalanceCreatesStripeSession() throws Exception {
    BalanceTopUpRequest request = new BalanceTopUpRequest();
    request.setAmount(25.0);

    Session mockSession = Mockito.mock(Session.class);
    when(mockSession.getId()).thenReturn("sess_123");
    when(mockSession.getUrl()).thenReturn("https://stripe.com/session");

    try (MockedStatic<Session> mockedSession = Mockito.mockStatic(Session.class)) {
      mockedSession
          .when(() -> Session.create(any(SessionCreateParams.class)))
          .thenReturn(mockSession);

      mockMvc
          .perform(
              post("/api/v1/driver/1/balance")
                  .contentType("application/json")
                  .content(
                      """
                      {
                          "amount": 25.0,
                          "simulateSuccess": false
                      }
                      """))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.sessionId").value("sess_123"))
          .andExpect(jsonPath("$.url").value("https://stripe.com/session"));
    }
  }

  @Test
  @DisplayName("Checkout success updates driver balance from session metadata")
  @Requirement("ET-42")
  void testFinalizeTopUpUpdatesBalance() throws Exception {
    Session mockSession = Mockito.mock(Session.class);
    Map<String, String> metadata = new HashMap<>();
    metadata.put("userId", "1");
    metadata.put("amount", "25.0");

    when(mockSession.getMetadata()).thenReturn(metadata);

    try (MockedStatic<Session> mockedSession = Mockito.mockStatic(Session.class)) {
      mockedSession.when(() -> Session.retrieve("sess_123")).thenReturn(mockSession);

      mockMvc
          .perform(get("/api/v1/driver/checkout-success").param("session_id", "sess_123"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.status").value("success"));
    }

    verify(driverService).addBalanceToDriver(1L, 25.0);
  }
}
