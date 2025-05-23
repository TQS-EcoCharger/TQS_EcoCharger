package pt.ua.tqs.ecocharger.ecocharger.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

import pt.ua.tqs.ecocharger.ecocharger.models.Car;
import pt.ua.tqs.ecocharger.ecocharger.models.Driver;
import pt.ua.tqs.ecocharger.ecocharger.repository.CarRepository;
import pt.ua.tqs.ecocharger.ecocharger.repository.DriverRepository;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.DriverService;
import pt.ua.tqs.ecocharger.ecocharger.utils.NotFoundException;

@Service
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final CarRepository carRepository;

    public DriverServiceImpl(DriverRepository driverRepository, CarRepository carRepository) {
        this.carRepository = carRepository;
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
        Optional<Driver> existingDriverOptional = driverRepository.findById(id);
        if (existingDriverOptional.isEmpty()) {
            throw new NotFoundException("Driver not found with id: " + id);
        }
        Driver existingDriver = existingDriverOptional.get();
        existingDriver.setName(driver.getName());
        existingDriver.setEmail(driver.getEmail());
        existingDriver.setPassword(driver.getPassword());
        return saveDriver(existingDriver);
    }

    @Override
    public Driver addCarToDriver(Long id, Long carId) {
        Driver driver = driverRepository.findById(id).orElseThrow(() -> new NotFoundException("Driver not found with id: " + id));
        Car car = carRepository.findById(carId).orElseThrow(() -> new NotFoundException("Car not found with id: " + carId));
        if (driver.getCars().contains(car)) {
            throw new IllegalArgumentException("Car already assigned to driver");
        }
        driver.getCars().add(car);
        return saveDriver(driver);
    }

    @Override
    public Driver removeCarFromDriver(Long id, Long carId) {
        Driver driver = driverRepository.findById(id).orElseThrow(() -> new NotFoundException("Driver not found with id: " + id));
        Car car = carRepository.findById(carId).orElseThrow(() -> new NotFoundException("Car not found with id: " + carId));
        if (!driver.getCars().contains(car)) {
            throw new IllegalArgumentException("Car not assigned to driver");
        }
        driver.getCars().remove(car);
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
