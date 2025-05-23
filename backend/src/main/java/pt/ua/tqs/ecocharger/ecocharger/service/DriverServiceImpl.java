package pt.ua.tqs.ecocharger.ecocharger.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import pt.ua.tqs.ecocharger.ecocharger.models.Driver;
import pt.ua.tqs.ecocharger.ecocharger.repository.DriverRepository;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.DriverService;
import pt.ua.tqs.ecocharger.ecocharger.utils.NotFoundException;

@Service
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;

    public DriverServiceImpl(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Override
    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    @Override
    public Driver getDriverById(Long id) {
        Optional<Driver> driver = driverRepository.findById(id);
        if (driver.isPresent()) {
            return driver.get();
        } else {
            throw new NotFoundException("Driver not found with id: " + id);
        }
    }

    @Override
    public Driver createDriver(Driver driver) {
        return saveDriver(driver);
    }

    @Override
    public Driver updateDriver(Long id, Driver driver) {
        if (!driverRepository.existsById(id)) {
            throw new NotFoundException("Driver not found with id: " + id);
        }
        driver.setId(id);
        return saveDriver(driver);
    }

    @Override
    public Driver saveDriver(Driver driver) {
        return driverRepository.save(driver);
    }

    @Override
    public void deleteDriver(Long id) {
        driverRepository.deleteById(id);
    }
    
}
