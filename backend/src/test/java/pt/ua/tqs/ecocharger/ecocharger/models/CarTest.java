package pt.ua.tqs.ecocharger.ecocharger.models;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Year;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CarTest {

  private static Car car1;

  @BeforeAll
  public static void setUp() {
    car1 = new Car(1L, "Car 1", "Make 1", "Model 1", 2020, "AB-C1-23", 50.0, 40.0, 100.0, 15.0);
  }

  @Test
  public void createNewCar() {
    Car newCar = new Car();
    assertEquals("", newCar.getName());
    assertEquals("", newCar.getMake());
    assertEquals("", newCar.getModel());
    assertEquals(Year.now().getValue(), newCar.getManufacture_year());
    assertEquals("", newCar.getLicensePlate());
    assertEquals(0.0, newCar.getBatteryCapacity());
    assertEquals(0.0, newCar.getBatteryLevel());
    assertEquals(0.0, newCar.getKilometers());
    assertEquals(0.0, newCar.getConsumption());

    Car newCar2 =
        new Car(1L, "Car 1", "Make 1", "Model 1", 2020, "AB-C1-23", 50.0, 30.0, 100.0, 15.0);
    assertEquals(1L, newCar2.getId());
    assertEquals("Car 1", newCar2.getName());
    assertEquals("Make 1", newCar2.getMake());
    assertEquals("Model 1", newCar2.getModel());
    assertEquals(2020, newCar2.getManufacture_year());
    assertEquals("AB-C1-23", newCar2.getLicensePlate());
    assertEquals(50.0, newCar2.getBatteryCapacity());
    assertEquals(30.0, newCar2.getBatteryLevel());
    assertEquals(100.0, newCar2.getKilometers());
    assertEquals(15.0, newCar2.getConsumption());
  }

  @Test
  public void testCarSetters() {
    assertThrows(IllegalArgumentException.class, () -> car1.setId(-1L));
    car1.setId(10L);
    assertEquals(10L, car1.getId());

    assertThrows(IllegalArgumentException.class, () -> car1.setName(null));
    assertThrows(IllegalArgumentException.class, () -> car1.setName(""));
    assertThrows(IllegalArgumentException.class, () -> car1.setName(" "));
    car1.setName("New Car");
    assertEquals("New Car", car1.getName());

    assertThrows(IllegalArgumentException.class, () -> car1.setMake(null));
    assertThrows(IllegalArgumentException.class, () -> car1.setMake(""));
    assertThrows(IllegalArgumentException.class, () -> car1.setMake(" "));
    car1.setMake("New Make");
    assertEquals("New Make", car1.getMake());

    assertThrows(IllegalArgumentException.class, () -> car1.setModel(null));
    assertThrows(IllegalArgumentException.class, () -> car1.setModel(""));
    assertThrows(IllegalArgumentException.class, () -> car1.setModel(" "));
    car1.setModel("New Model");
    assertEquals("New Model", car1.getModel());

    assertThrows(IllegalArgumentException.class, () -> car1.setManufacture_Year(-1));
    assertThrows(IllegalArgumentException.class, () -> car1.setManufacture_Year(0));
    car1.setManufacture_Year(2023);
    assertEquals(2023, car1.getManufacture_year());

    assertThrows(IllegalArgumentException.class, () -> car1.setLicensePlate(null));
    assertThrows(IllegalArgumentException.class, () -> car1.setLicensePlate(""));
    assertThrows(IllegalArgumentException.class, () -> car1.setLicensePlate(" "));
    assertThrows(IllegalArgumentException.class, () -> car1.setLicensePlate("AB-C1-23-45"));
    car1.setLicensePlate("AB-C1-23");
    assertEquals("AB-C1-23", car1.getLicensePlate());
    car1.setLicensePlate("ABC2D3");
    assertEquals("AB-C2-D3", car1.getLicensePlate());

    assertThrows(IllegalArgumentException.class, () -> car1.setBatteryCapacity(-1.0));
    assertThrows(IllegalArgumentException.class, () -> car1.setBatteryCapacity(0.0));
    car1.setBatteryCapacity(100.0);
    assertEquals(100.0, car1.getBatteryCapacity());

    assertThrows(IllegalArgumentException.class, () -> car1.setBatteryLevel(-1.0));
    assertThrows(IllegalArgumentException.class, () -> car1.setBatteryLevel(110.0));
    car1.setBatteryLevel(50.0);
    assertEquals(50.0, car1.getBatteryLevel());

    assertThrows(IllegalArgumentException.class, () -> car1.setKilometers(-1.0));
    car1.setKilometers(700.0);
    assertEquals(700.0, car1.getKilometers());

    assertThrows(IllegalArgumentException.class, () -> car1.setConsumption(-1.0));
    car1.setConsumption(10.0);
    assertEquals(10.0, car1.getConsumption());
  }
}
