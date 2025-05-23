package pt.ua.tqs.ecocharger.ecocharger.models;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.Year;
import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.lang.IllegalArgumentException;

import pt.ua.tqs.ecocharger.ecocharger.models.Car;

public class CarTest {
    
    private static Car car1;
    private static Car car2;
    private static Car car3;
    private static Car car4;
    private static Car car5;
    
    @BeforeAll
    public static void setUp() {
        car1 = new Car(1L, "Car 1", "Make 1", "Model 1", 2020, "AB-C1-23", 50.0, 40.0, 100.0, 15.0);
        car2 = new Car(2L, "Car 2", "Make 2", "Model 2", 2021, "DE-F4-56", 60.0, 30.0, 200.0, 12.0);
        car3 = new Car(3L, "Car 3", "Make 3", "Model 3", 2022, "GH-I7-89", 70.0, 20.0, 300.0, 10.0);
        car4 = new Car(4L, "Car 4", "Make 4", "Model 4", 2023, "JK-L0-12", 80.0, 10.0, 400.0, 8.0);
        car5 = new Car(5L, "Car 5", "Make 5", "Model 5", 2024, "MN-O3-45", 90.0, 10.0, 500.0, 6.0);
    }

    @Test
    public void createNewCar() {
        Car newCar = new Car();
        assertEquals("", newCar.getName());
        assertEquals("", newCar.getMake());
        assertEquals("", newCar.getModel());
        assertEquals(Year.now().getValue(), newCar.getYear());
        assertEquals("", newCar.getLicensePlate());
        assertEquals(0.0, newCar.getBatteryCapacity());
        assertEquals(0.0, newCar.getBatteryLevel());
        assertEquals(0.0, newCar.getKilometers());
        assertEquals(0.0, newCar.getConsumption());

        Car newCar2 = new Car(1L, "Car 1", "Make 1", "Model 1", 2020, "AB-C1-23", 50.0, 30.0, 100.0, 15.0);
        assertEquals(1L, newCar2.getId());
        assertEquals("Car 1", newCar2.getName());
        assertEquals("Make 1", newCar2.getMake());
        assertEquals("Model 1", newCar2.getModel());
        assertEquals(2020, newCar2.getYear());
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

        assertThrows(IllegalArgumentException.class, () -> car1.setYear(-1));
        assertThrows(IllegalArgumentException.class, () -> car1.setYear(0));
        car1.setYear(2023);
        assertEquals(2023, car1.getYear());

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
