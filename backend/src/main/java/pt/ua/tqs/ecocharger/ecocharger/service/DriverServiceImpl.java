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
        Optional<Driver> existingDriver = driverRepository.findByEmail(driver.getEmail());
        if (existingDriver.isPresent()) {
            throw new IllegalArgumentException("Driver with email " + driver.getEmail() + " already exists");
        }
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
    public Driver addCarToDriver(Long driverId, Car car) {
        Driver driver = driverRepository.findById(driverId).orElseThrow(() -> new NotFoundException("Driver not found with id: " + driverId));
        
        Car newCar = new Car();
        newCar.setName(car.getName());
        newCar.setMake(car.getMake());
        newCar.setModel(car.getModel());
        newCar.setYear(car.getYear());
        newCar.setLicensePlate(car.getLicensePlate());
        newCar.setBatteryCapacity(car.getBatteryCapacity());
        newCar.setBatteryLevel(car.getBatteryLevel());
        newCar.setConsumption(car.getConsumption());
        newCar.setEnabled(true);
        carRepository.save(newCar);
        driver.getCars().add(newCar);
        return saveDriver(driver);
    }
    
    
    

    @Override
    public Driver editCarFromDriver(Long id, Long carId, Car car) {
        Driver driver = driverRepository.findById(id).orElseThrow(() -> new NotFoundException("Driver not found with id: " + id));
        Car existingCar = carRepository.findById(carId).orElseThrow(() -> new NotFoundException("Car not found with id: " + carId));
        if (!driver.getCars().contains(existingCar)) {
            throw new IllegalArgumentException("Car not assigned to driver");
        }
        existingCar.setName(car.getName());
        existingCar.setMake(car.getMake());
        existingCar.setModel(car.getModel());
        existingCar.setYear(car.getYear());
        existingCar.setLicensePlate(car.getLicensePlate());
        existingCar.setBatteryCapacity(car.getBatteryCapacity());
        existingCar.setBatteryLevel(car.getBatteryLevel());
        existingCar.setConsumption(car.getConsumption());
        existingCar.setEnabled(car.isEnabled());
        carRepository.save(existingCar);
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
        Optional<Driver> driver = driverRepository.findById(id);
        if (driver.isEmpty()) {
            throw new NotFoundException("Driver not found with id: " + id);
        }
        driverRepository.deleteById(id);
    }
    
}
