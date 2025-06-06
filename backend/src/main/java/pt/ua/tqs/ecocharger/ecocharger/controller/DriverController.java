package pt.ua.tqs.ecocharger.ecocharger.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pt.ua.tqs.ecocharger.ecocharger.models.Car;
import pt.ua.tqs.ecocharger.ecocharger.models.Driver;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.DriverService;
import pt.ua.tqs.ecocharger.ecocharger.utils.NotFoundException;

@RestController
@RequestMapping("/api/v1/driver")
@Tag(name = "Drivers", description = "Operations related to drivers and their cars")
public class DriverController {

  private final DriverService driverService;

  public DriverController(DriverService driverService) {
    this.driverService = driverService;
  }

  @Operation(summary = "Get all drivers")
  @GetMapping("/")
  public ResponseEntity<List<Driver>> getAllDrivers() {
    return ResponseEntity.ok(driverService.getAllDrivers());
  }

  @Operation(summary = "Get driver by ID")
  @GetMapping("/{id}")
  public ResponseEntity<Object> getDriverById(
      @Parameter(description = "ID of the driver") @PathVariable Long id) {
    try {
      Driver existingDriver = driverService.getDriverById(id);
      return ResponseEntity.ok(existingDriver);
    } catch (NotFoundException e) {
      return ResponseEntity.status(404).body(e.getMessage());
    }
  }

  @Operation(summary = "Create a new driver")
  @PostMapping("/")
  public ResponseEntity<Object> createDriver(@RequestBody Driver driver) {
    try {
      Driver newDriver = driverService.createDriver(driver);
      return ResponseEntity.ok(newDriver);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(400).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(500).body("An error occurred while creating the driver.");
    }
  }

  @Operation(summary = "Update a driver by ID")
  @PutMapping("/{id}")
  public ResponseEntity<Object> updateDriver(
      @Parameter(description = "ID of the driver") @PathVariable Long id,
      @RequestBody Driver driver) {
    try {
      Driver updatedDriver = driverService.updateDriver(id, driver);
      return ResponseEntity.ok(updatedDriver);
    } catch (NotFoundException e) {
      return ResponseEntity.status(404).body(e.getMessage());
    }
  }

  @Operation(summary = "Add a car to a driver")
  @PatchMapping("{id}/cars/")
  public ResponseEntity<Object> addCarToDriver(
      @Parameter(description = "ID of the driver") @PathVariable Long id, @RequestBody Car car) {
    try {
      Driver updatedDriver = driverService.addCarToDriver(id, car);
      return ResponseEntity.ok(updatedDriver);
    } catch (NotFoundException e) {
      return ResponseEntity.status(404).body(e.getMessage());
    }
  }

  @Operation(summary = "Remove a car from a driver")
  @DeleteMapping("{id}/cars/{carId}")
  public ResponseEntity<Object> removeCarFromDriver(
      @Parameter(description = "ID of the driver") @PathVariable Long id,
      @Parameter(description = "ID of the car to remove") @PathVariable Long carId) {
    try {
      Driver existingDriver = driverService.removeCarFromDriver(id, carId);
      return ResponseEntity.ok(existingDriver);
    } catch (NotFoundException e) {
      return ResponseEntity.status(404).body(e.getMessage());
    }
  }

  @Operation(summary = "Edit a driver's car")
  @PatchMapping("{id}/cars/{carId}")
  public ResponseEntity<Object> editCarFromDriver(
      @Parameter(description = "ID of the driver") @PathVariable Long id,
      @Parameter(description = "ID of the car to edit") @PathVariable Long carId,
      @RequestBody Car car) {
    try {
      Driver updatedDriver = driverService.editCarFromDriver(id, carId, car);
      return ResponseEntity.ok(updatedDriver);
    } catch (NotFoundException e) {
      return ResponseEntity.status(404).body(e.getMessage());
    }
  }

  @Operation(summary = "Delete a driver by ID")
  @DeleteMapping("/{id}")
  public ResponseEntity<Object> deleteDriver(
      @Parameter(description = "ID of the driver to delete") @PathVariable Long id) {
    try {
      driverService.deleteDriver(id);
      return ResponseEntity.noContent().build();
    } catch (NotFoundException e) {
      return ResponseEntity.status(404).body(e.getMessage());
    }
  }
}
