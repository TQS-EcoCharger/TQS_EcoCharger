package pt.ua.tqs.ecocharger.ecocharger.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pt.ua.tqs.ecocharger.ecocharger.models.ChargingStation;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.ChargingStationService;

@RestController
@RequestMapping("/api/v1/chargingStations")
@Tag(name = "Charging Stations", description = "Endpoints for managing charging stations")
public class ChargingStationController {

  private final ChargingStationService chargingStationService;

  public ChargingStationController(ChargingStationService chargingStationService) {
    this.chargingStationService = chargingStationService;
  }

  @Operation(summary = "Create a new charging station")
  @PostMapping
  public ResponseEntity<ChargingStation> createStation(
      @RequestBody ChargingStation station) {
    ChargingStation savedStation = chargingStationService.createStation(station);
    return ResponseEntity.ok(savedStation);
  }

  @Operation(summary = "Get all charging stations by city name")
  @GetMapping("/city/{cityName}")
  public ResponseEntity<List<ChargingStation>> getStationsByCity(
      @Parameter(description = "Name of the city") @PathVariable String cityName) {
    List<ChargingStation> stations = chargingStationService.getAllStationsByCityName(cityName);
    return ResponseEntity.ok(stations);
  }

  @Operation(summary = "Delete a charging station by ID")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteStation(
      @Parameter(description = "ID of the charging station to delete") @PathVariable Long id) {
    chargingStationService.deleteStation(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Get all charging stations")
  @GetMapping
  public ResponseEntity<List<ChargingStation>> getAllStations() {
    List<ChargingStation> stations = chargingStationService.getAllStations();
    return ResponseEntity.ok(stations);
  }
}
