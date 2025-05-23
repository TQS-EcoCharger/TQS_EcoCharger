package pt.ua.tqs.ecocharger.ecocharger.service.interfaces;

import java.util.List;
import java.util.Optional;
import pt.ua.tqs.ecocharger.ecocharger.models.Driver;

public interface DriverService {
    
    List<Driver> getAllDrivers();
    Driver getDriverById(Long id);
    Driver saveDriver(Driver driver);
    Driver createDriver(Driver driver);
    Driver updateDriver(Long id, Driver driver);
    void deleteDriver(Long id);
}
