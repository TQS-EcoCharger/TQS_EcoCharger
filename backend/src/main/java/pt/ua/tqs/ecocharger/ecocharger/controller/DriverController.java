package pt.ua.tqs.ecocharger.ecocharger.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
            return ResponseEntity.ok(driverService.getDriverById(id));
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PostMapping("/")
    public ResponseEntity<Object> createDriver(@RequestBody Driver driver) {
        try {
            return ResponseEntity.ok(driverService.createDriver(driver));
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateDriver(@PathVariable Long id, @RequestBody Driver driver) {
        try {
            return ResponseEntity.ok(driverService.updateDriver(id, driver));
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PatchMapping("{id}/cars/{carId}")
    public ResponseEntity<Object> addCarToDriver(@PathVariable Long id, @PathVariable Long carId) {
        try {
            return ResponseEntity.ok(driverService.addCarToDriver(id, carId));
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @DeleteMapping("{id}/cars/{carId}")
    public ResponseEntity<Object> removeCarFromDriver(@PathVariable Long id, @PathVariable Long carId) {
        try {
            return ResponseEntity.ok(driverService.removeCarFromDriver(id, carId));
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
