package pt.ua.tqs.ecocharger.ecocharger.service;

import static org.junit.jupiter.api.Assertions.*;
// TODO: change to non-wildcard imports
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import app.getxray.xray.junit.customjunitxml.annotations.Requirement;
import pt.ua.tqs.ecocharger.ecocharger.models.Car;
import pt.ua.tqs.ecocharger.ecocharger.models.Driver;
import pt.ua.tqs.ecocharger.ecocharger.repository.CarRepository;
import pt.ua.tqs.ecocharger.ecocharger.repository.DriverRepository;
import pt.ua.tqs.ecocharger.ecocharger.utils.NotFoundException;

public class DriverServiceTest {

  @Mock private DriverRepository driverRepository;

  @Mock private CarRepository carRepository;

  @InjectMocks private DriverServiceImpl driverService;

  private Driver driver1;
  private Driver driver2;
  private Driver driver3;
  private Driver driver4;
  private Driver driver5;

  private Car car1;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    driver1 = new Driver(1L, "johndoe@example.com", "password", "John Doe", true);
    driver2 = new Driver(2L, "katherinedoe@example.com", "password2", "Katherine Doe", true);
    driver3 = new Driver(3L, "paulbrook@example.com", "password3", "Paul Brook", true);
    driver4 = new Driver(4L, "viktorpineapple@example.com", "password4", "Viktor Pineapple", true);
    driver5 = new Driver(5L, "sergeyaleixov@example.com", "password5", "Sergey Aleixov", true);

    car1 = new Car(null, "Car 1", "Make 1", "Model 1", 2020, "AB-C1-23", 50.0, 40.0, 100.0, 15.0);

    when(driverRepository.findById(1L)).thenReturn(Optional.of(driver1));
    when(driverRepository.findById(2L)).thenReturn(Optional.of(driver2));
    when(driverRepository.findById(3L)).thenReturn(Optional.of(driver3));
    when(driverRepository.findById(4L)).thenReturn(Optional.of(driver4));
    when(driverRepository.findById(5L)).thenReturn(Optional.of(driver5));

    when(driverRepository.findAll())
        .thenReturn(List.of(driver1, driver2, driver3, driver4, driver5));
  }

  @Test
  @DisplayName("Get All Drivers")
  @Requirement("ET-34")
  void testGetAllDrivers() throws Exception {
    List<Driver> drivers = driverService.getAllDrivers();
    assertEquals(5, drivers.size());
    assertEquals(driver1, drivers.get(0));
    assertEquals(driver2, drivers.get(1));
    assertEquals(driver3, drivers.get(2));
    assertEquals(driver4, drivers.get(3));
    assertEquals(driver5, drivers.get(4));
    verify(driverRepository, times(1)).findAll();
  }

  @Test
  @DisplayName("Get Driver By ID")
  @Requirement("ET-34")
  void testGetDriverById() throws Exception {
    Driver driver = driverService.getDriverById(1L);
    assertEquals(driver1, driver);
    verify(driverRepository, times(1)).findById(1L);
  }

  @Test
  @DisplayName("Create Driver")
  @Requirement("ET-34")
  void testCreateDriver() throws Exception {
    Driver newDriver =
        new Driver(null, "mariahcarey@example.com", "password6", "Mariah Carey", true);
    when(driverRepository.findByEmail(newDriver.getEmail())).thenReturn(Optional.empty());
    when(driverRepository.save(newDriver)).thenReturn(newDriver);
    Driver createdDriver = driverService.createDriver(newDriver);
    assertEquals(newDriver, createdDriver);
    verify(driverRepository, times(1)).save(newDriver);
  }

  @Test
  @DisplayName("Update Driver")
  @Requirement("ET-34")
  void testUpdateDriver() throws Exception {
    Driver updatedDriver = new Driver(1L, "johndoh@example.com", "newpassword", "John Doh", true);
    when(driverRepository.save(updatedDriver)).thenReturn(updatedDriver);
    Driver result = driverService.updateDriver(1L, updatedDriver);
    assertEquals(updatedDriver, result);
    verify(driverRepository, times(1)).findById(1L);
    verify(driverRepository, times(1)).save(updatedDriver);
  }

  @Test
  @DisplayName("Delete Driver")
  @Requirement("ET-34")
  void testDeleteDriver() throws Exception {
    driverService.deleteDriver(1L);
    verify(driverRepository, times(1)).deleteById(1L);
  }

  @Test
  @DisplayName("Get Driver By ID Not Found")
  @Requirement("ET-34")
  void testGetDriverByIdNotFound() throws Exception {
    when(driverRepository.findById(6L)).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> driverService.getDriverById(6L));
    verify(driverRepository, times(1)).findById(6L);
  }

  @Test
  @DisplayName("Create Driver Already Exists")
  @Requirement("ET-34")
  void testCreateDriverAlreadyExists() throws Exception {
    when(driverRepository.findByEmail(driver1.getEmail())).thenReturn(Optional.of(driver1));
    assertThrows(IllegalArgumentException.class, () -> driverService.createDriver(driver1));
    verify(driverRepository, times(1)).findByEmail(driver1.getEmail());
  }

  @Test
  @DisplayName("Update Driver Not Found")
  @Requirement("ET-34")
  void testUpdateDriverNotFound() throws Exception {
    when(driverRepository.findById(6L)).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> driverService.updateDriver(6L, driver1));
    verify(driverRepository, times(1)).findById(6L);
  }

  @Test
  @DisplayName("Delete Driver Not Found")
  @Requirement("ET-34")
  void testDeleteDriverNotFound() throws Exception {
    when(driverRepository.findById(6L)).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> driverService.deleteDriver(6L));
    verify(driverRepository, times(1)).findById(6L);
  }

  @Test
  @DisplayName("Add Car To Driver")
  @Requirement("ET-34")
  void testAddCarToDriver() throws Exception {
    when(carRepository.findById(1L)).thenReturn(Optional.of(car1));
    Driver driverWithCar = new Driver();
    driverWithCar.setId(driver1.getId());
    driverWithCar.setEmail(driver1.getEmail());
    driverWithCar.setPassword(driver1.getPassword());
    driverWithCar.setName(driver1.getName());
    driverWithCar.addCar(car1);
    when(driverRepository.save(any(Driver.class))).thenReturn(driverWithCar);
    Driver updatedDriver = driverService.addCarToDriver(1L, car1);
    assertEquals(driver1.getCars(), updatedDriver.getCars());
    verify(driverRepository, times(1)).findById(1L);
    verify(driverRepository, times(1)).save(driver1);
  }

  @Test
  @DisplayName("Add Car To Driver Not Found")
  @Requirement("ET-34")
  void testAddCarToDriverNotFound() throws Exception {
    when(driverRepository.findById(6L)).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> driverService.addCarToDriver(6L, car1));
    verify(driverRepository, times(1)).findById(6L);
  }

  @Test
  @DisplayName("Edit a car in a driver")
  @Requirement("ET-34")
  void testEditCarFromDriver() throws Exception {
    driver1.addCar(car1);
    when(carRepository.findById(1L)).thenReturn(Optional.of(car1));
    when(driverRepository.save(driver1)).thenReturn(driver1);
    Car updatedCar =
        new Car(
            1L,
            "Updated Car",
            "Updated Make",
            "Updated Model",
            2021,
            "AB-C2-34",
            60.0,
            50.0,
            120.0,
            18.0);
    Driver updatedDriver = driverService.editCarFromDriver(1L, 1L, updatedCar);
    assertEquals(1, updatedDriver.getCars().size());
    assertEquals(updatedCar.getName(), updatedDriver.getCars().get(0).getName());
    verify(driverRepository, times(1)).findById(1L);
    verify(carRepository, times(1)).findById(1L);
    verify(driverRepository, times(1)).save(driver1);
  }

  @Test
  @DisplayName("Remove Car From Driver")
  @Requirement("ET-34")
  void testRemoveCarFromDriver() throws Exception {
    driver1.addCar(car1);
    when(carRepository.findById(1L)).thenReturn(Optional.of(car1));
    when(driverRepository.save(driver1)).thenReturn(driver1);
    Driver updatedDriver = driverService.removeCarFromDriver(1L, 1L);
    assertEquals(driver1, updatedDriver);
    assertEquals(0, updatedDriver.getCars().size());
    verify(driverRepository, times(1)).findById(1L);
    verify(carRepository, times(1)).findById(1L);
    verify(driverRepository, times(1)).save(driver1);
  }

  @Test
  @DisplayName("Remove Car From Driver Not Found")
  @Requirement("ET-34")
  void testRemoveCarFromDriverNotFound() throws Exception {
    when(driverRepository.findById(6L)).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> driverService.removeCarFromDriver(6L, 1L));
    verify(driverRepository, times(1)).findById(6L);
  }

  @Test
  @DisplayName("Remove Car From Driver Car Not Found")
  @Requirement("ET-34")
  void testRemoveCarFromDriverCarNotFound() throws Exception {
    when(carRepository.findById(6L)).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> driverService.removeCarFromDriver(1L, 6L));
    verify(carRepository, times(1)).findById(6L);
  }

  @Test
  @DisplayName("Remove Car From Driver Car Not Assigned")
  @Requirement("ET-34")
  void testRemoveCarFromDriverCarNotAssigned() throws Exception {
    when(carRepository.findById(1L)).thenReturn(Optional.of(car1));
    driver1.removeCar(car1);
    when(driverRepository.save(driver1)).thenReturn(driver1);
    assertThrows(IllegalArgumentException.class, () -> driverService.removeCarFromDriver(1L, 1L));
    verify(driverRepository, times(1)).findById(1L);
    verify(carRepository, times(1)).findById(1L);
  }
}
