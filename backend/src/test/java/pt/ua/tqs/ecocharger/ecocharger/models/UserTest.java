package pt.ua.tqs.ecocharger.ecocharger.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;

public class UserTest {

  @Test
  @DisplayName("Test User constructor and getters")
  void testUserConstructorAndGetters() {
    User user = new User(1L, "john@example.com", "password123", "John Doe", true);

    assertEquals(1L, user.getId());
    assertEquals("john@example.com", user.getEmail());
    assertEquals("password123", user.getPassword());
    assertEquals("John Doe", user.getName());
    assertTrue(user.isEnabled());
  }

  @Test
  @DisplayName("Test User setters")
  void testUserSetters() {
    User user = new User();
    user.setId(2L);
    user.setEmail("jane@example.com");
    user.setPassword("pass456");
    user.setName("Jane Doe");
    user.setEnabled(false);

    assertEquals(2L, user.getId());
    assertEquals("jane@example.com", user.getEmail());
    assertEquals("pass456", user.getPassword());
    assertEquals("Jane Doe", user.getName());
    assertFalse(user.isEnabled());
  }

  @Test
  @DisplayName("Test User toString method")
  void testUserEquality() {
    User user1 = new User(1L, "a@b.com", "pass", "Name", true);
    User user2 = new User(1L, "a@b.com", "pass", "Name", true);

    assertEquals(user1, user2);
    assertEquals(user1.hashCode(), user2.hashCode());
  }

  @Test
  void createDriver() {
    Driver driver = new Driver(1L, "john@example.com", "password123", "John Doe", true);

    assertEquals(1L, driver.getId());
    assertEquals("john@example.com", driver.getEmail());
    assertEquals("password123", driver.getPassword());
    assertEquals("John Doe", driver.getName());
    assertTrue(driver.isEnabled());
    assertNotNull(driver.getCars());
    assertTrue(driver.getCars().isEmpty());

    Car car = new Car();
    driver.addCar(car);
    assertEquals(1, driver.getCars().size());
    assertEquals(car, driver.getCars().get(0));
    driver.removeCar(car);
    assertTrue(driver.getCars().isEmpty());
  }

  @Test
  void createChargingOperator() {
    ChargingOperator operator = new ChargingOperator(5L, "charging@operator.com", "securepass", "Operator Name", true);

    assertEquals(5L, operator.getId());
    assertEquals("charging@operator.com", operator.getEmail());
    assertEquals("securepass", operator.getPassword());
    assertEquals("Operator Name", operator.getName());
    assertTrue(operator.isEnabled());
    assertNotNull(operator.getChargingStations());
    assertTrue(operator.getChargingStations().isEmpty());

    ChargingStation station = new ChargingStation();
    operator.addChargingStation(station);
    assertEquals(1, operator.getChargingStations().size());
    assertEquals(station, operator.getChargingStations().get(0));
    operator.removeChargingStation(station);
    assertTrue(operator.getChargingStations().isEmpty());
  }
}
