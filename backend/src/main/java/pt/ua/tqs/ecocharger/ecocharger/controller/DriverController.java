package pt.ua.tqs.ecocharger.ecocharger.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import pt.ua.tqs.ecocharger.ecocharger.models.Car;
import pt.ua.tqs.ecocharger.ecocharger.models.Driver;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.DriverService;
import pt.ua.tqs.ecocharger.ecocharger.utils.NotFoundException;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/api/v1/driver")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }
    
    @GetMapping("/")
    public ResponseEntity<List<Driver>> getAllDrivers() {
        return ResponseEntity.ok(driverService.getAllDrivers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getDriverById(@PathVariable Long id) {
        try {
            Driver existingDriver = driverService.getDriverById(id);
            System.out.println("Driver found: " + existingDriver);
            return ResponseEntity.ok(existingDriver);
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PostMapping("/")
    public ResponseEntity<Object> createDriver(@RequestBody Driver driver) {
        Driver newDriver = driverService.createDriver(driver);
        return ResponseEntity.ok(newDriver);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateDriver(@PathVariable Long id, @RequestBody Driver driver) {
        try {
            Driver updatedDriver = driverService.updateDriver(id, driver);
            return ResponseEntity.ok(updatedDriver);
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PatchMapping("{id}/cars/")
    public ResponseEntity<Object> addCarToDriver(@PathVariable Long id, @RequestBody Car car) {
        try {
            Driver updatedDriver = driverService.addCarToDriver(id, car);
            return ResponseEntity.ok(updatedDriver);
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @DeleteMapping("{id}/cars/{carId}")
    public ResponseEntity<Object> removeCarFromDriver(@PathVariable Long id, @PathVariable Long carId) {
        try {
            Driver existingDriver = driverService.removeCarFromDriver(id, carId);
            return ResponseEntity.ok(existingDriver);
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteDriver(@PathVariable Long id) {
        try {
            driverService.deleteDriver(id);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
