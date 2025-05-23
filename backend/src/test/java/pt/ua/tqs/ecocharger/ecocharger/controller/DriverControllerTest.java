package pt.ua.tqs.ecocharger.ecocharger.controller;

import java.util.List;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import pt.ua.tqs.ecocharger.ecocharger.config.SecurityDisableConfig;
import pt.ua.tqs.ecocharger.ecocharger.models.Car;
import pt.ua.tqs.ecocharger.ecocharger.models.Driver;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.DriverService;
import pt.ua.tqs.ecocharger.ecocharger.utils.NotFoundException;

import static org.mockito.Mockito.when;
// TODO: Change wildcard to single imports
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.hamcrest.Matchers.hasSize;

import org.junit.jupiter.api.BeforeEach;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;


@WebMvcTest(DriverController.class)
@Import(SecurityDisableConfig.class)
public class DriverControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @MockitoBean
    private DriverService driverService;

    private Driver driver1;
    private Driver driver2;
    private Driver driver3;
    private Driver driver4;
    private Driver driver5;


    @BeforeEach
    public void setUp() {
        driver1 = new Driver(1L, "johndoe@example.com", "password", "John Doe", true);
        driver2 = new Driver(2L, "katherinedoe@example.com", "password2", "Katherine Doe", true);
        driver3 = new Driver(3L, "paulbrook@example.com", "password3", "Paul Brook", true);
        driver4 = new Driver(4L, "viktorpineapple@example.com", "password4", "Viktor Pineapple", true);
        driver5 = new Driver(5L, "sergeyaleixov@example.com", "password5", "Sergey Aleixov", true);

        when(driverService.getAllDrivers()).thenReturn(List.of(driver1, driver2, driver3, driver4, driver5));
        when(driverService.getDriverById(1L)).thenReturn(driver1);
        when(driverService.getDriverById(2L)).thenReturn(driver2);
        when(driverService.getDriverById(3L)).thenReturn(driver3);
        when(driverService.getDriverById(4L)).thenReturn(driver4);
        when(driverService.getDriverById(5L)).thenReturn(driver5);
        when(driverService.getDriverById(6L)).thenThrow(new NotFoundException("Driver not found"));
    }


    @Test
    @DisplayName("Getting all drivers returns 200 OK and list in JSON")
    public void testGetAllDrivers() throws Exception {
        mockMvc.perform(get("/api/v1/driver/"))
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
    public void testGetDriverById() throws Exception {
        mockMvc.perform(get("/api/v1/driver/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    @DisplayName("Getting driver by ID returns 404 NOT FOUND if it does not exist")
    public void testGetDriverByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/driver/6"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$").value("Driver not found"));
    }

    @Test
    @DisplayName("Updating driver returns 200 OK and updated driver in JSON if it exists")
    public void testUpdateDriver() throws Exception {
        Driver updatedDriver = new Driver(null, "johnfantastic@example.com", "newpassword", "John Fantastic", true);
        driver1.setEmail(updatedDriver.getEmail());
        driver1.setPassword(updatedDriver.getPassword());
        driver1.setName(updatedDriver.getName());

        when(driverService.updateDriver(eq(1L), any(Driver.class))).thenReturn(driver1);

        mockMvc.perform(put("/api/v1/driver/1")
                .contentType("application/json")
                .content("{\"email\":\"johnfantastic@example.com\", \"password\":\"newpassword\", \"name\":\"John Fantastic\", \"enabled\":\"true\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(driver1.getEmail()));
    }

    @Test
    @DisplayName("Updating driver returns 404 NOT FOUND if it does not exist")
    public void testUpdateDriverNotFound() throws Exception {
        when(driverService.updateDriver(eq(6L), any(Driver.class))).thenThrow(new NotFoundException("Driver not found"));
        mockMvc.perform(put("/api/v1/driver/6")
                .contentType("application/json")
                .content("{\"email\":\"johnfantastic@example.com\", \"password\":\"newpassword\", \"name\":\"John Fantastic\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Driver not found"));
    }

    @Test
    @DisplayName("Adding car to driver returns 200 OK and updated driver in JSON if it exists")
    public void testAddCarToDriver() throws Exception {
        Car car = new Car(2L,"Tesla Model S", "Tesla", "Model S", 2022, "ABC123", 100.0, 50.0, 0.0, 0.0);
        driver1.addCar(car);
        when(driverService.addCarToDriver(1L, 2L)).thenReturn(driver1);

        mockMvc.perform(patch("/api/v1/driver/1/cars/2")
                .contentType("application/json")
                .content("{\"carId\":2}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cars[0].id").value(2));
    }

    @Test
    @DisplayName("Adding car to driver returns 404 NOT FOUND if driver does not exist")
    public void testAddCarToDriverNotFound() throws Exception {
        when(driverService.addCarToDriver(6L, 2L)).thenThrow(new NotFoundException("Driver not found"));
        mockMvc.perform(patch("/api/v1/driver/6/cars/2"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Driver not found"));
    }

    @Test
    @DisplayName("Adding car to driver returns 404 NOT FOUND if car does not exist")
    public void testAddCarToDriverCarNotFound() throws Exception {
        when(driverService.addCarToDriver(1L, 6L)).thenThrow(new NotFoundException("Car not found"));
        mockMvc.perform(patch("/api/v1/driver/1/cars/6")
                .contentType("application/json")
                .content("{\"carId\":6}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Car not found"));
    }

    @Test
    @DisplayName("Removing car from driver returns 200 OK and updated driver in JSON if it exists")
    public void testRemoveCarFromDriver() throws Exception {
        when(driverService.removeCarFromDriver(1L, 2L)).thenReturn(driver1);

        mockMvc.perform(delete("/api/v1/driver/1/cars/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(driver1.getEmail()));
    }

    @Test
    @DisplayName("Removing car from driver returns 404 NOT FOUND if driver does not exist")
    public void testRemoveCarFromDriverNotFound() throws Exception {
        when(driverService.removeCarFromDriver(6L, 2L)).thenThrow(new NotFoundException("Driver not found"));
        mockMvc.perform(delete("/api/v1/driver/6/cars/2")
                .contentType("application/json")
                .content("{\"carId\":2}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Driver not found"));
    }

    @Test
    @DisplayName("Removing car from driver returns 404 NOT FOUND if car does not exist")
    public void testRemoveCarFromDriverCarNotFound() throws Exception {
        when(driverService.removeCarFromDriver(1L, 6L)).thenThrow(new NotFoundException("Car not found"));
        mockMvc.perform(delete("/api/v1/driver/1/cars/6")
                .contentType("application/json")
                .content("{\"carId\":6}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Car not found"));
    }

    @Test
    @DisplayName("Deleting driver returns 200 OK if it exists")
    public void testDeleteDriver() throws Exception {
        mockMvc.perform(delete("/api/v1/driver/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deleting driver returns 404 NOT FOUND if it does not exist")
    public void testDeleteDriverNotFound() throws Exception {
        doThrow(new NotFoundException("Driver not found")).when(driverService).deleteDriver(6L);
        mockMvc.perform(delete("/api/v1/driver/6"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Driver not found"));
    }
}
