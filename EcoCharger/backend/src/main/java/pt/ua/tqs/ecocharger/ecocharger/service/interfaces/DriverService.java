package pt.ua.tqs.ecocharger.ecocharger.service.interfaces;

import java.util.List;

import pt.ua.tqs.ecocharger.ecocharger.models.Car;
import pt.ua.tqs.ecocharger.ecocharger.models.Driver;

public interface DriverService {

  List<Driver> getAllDrivers();

  boolean driverExists(Long id);

  Driver getDriverById(Long id);

  Driver saveDriver(Driver driver);

  Driver createDriver(Driver driver);

  Driver updateDriver(Long id, Driver driver);

  Driver addCarToDriver(Long id, Car car);

  Driver editCarFromDriver(Long id, Long carId, Car car);

  Driver removeCarFromDriver(Long id, Long carId);

  void deleteDriver(Long id);

  Driver addBalanceToDriver(Long id, Double amount);
}
