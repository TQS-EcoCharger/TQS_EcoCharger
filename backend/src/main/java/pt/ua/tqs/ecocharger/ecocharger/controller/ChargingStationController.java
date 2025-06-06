package pt.ua.tqs.ecocharger.ecocharger.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pt.ua.tqs.ecocharger.ecocharger.models.ChargingStation;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.ChargingStationService;
import pt.ua.tqs.ecocharger.ecocharger.utils.NotFoundException;

@RestController
@RequestMapping("/api/v1/chargingStations")
@Tag(name = "Charging Stations", description = "Endpoints for managing charging stations")
public class ChargingStationController {

  private final ChargingStationService chargingStationService;

  public ChargingStationController(ChargingStationService chargingStationService) {
    this.chargingStationService = chargingStationService;
  }

  @Operation(summary = "Create a new charging station",
             description = "Creates a new charging station with the provided details")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Charging station created successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid charging station data")
  })
  @PostMapping
  public ResponseEntity<ChargingStation> createStation(@RequestBody ChargingStation station) {
    ChargingStation savedStation = chargingStationService.createStation(station);
    return ResponseEntity.ok(savedStation);
  }

  @Operation(summary = "Get all charging stations by city name",
             description = "Retrieves all charging stations located in the specified city")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Charging stations retrieved successfully"),
      @ApiResponse(responseCode = "404", description = "No charging stations found for the given city")
  })
  @GetMapping("/city/{cityName}")
  public ResponseEntity<List<ChargingStation>> getStationsByCity(
      @Parameter(description = "Name of the city") @PathVariable String cityName) {
    List<ChargingStation> stations = chargingStationService.getAllStationsByCityName(cityName);
    return ResponseEntity.ok(stations);
  }

  @Operation(summary = "Delete a charging station by ID",
             description = "Deletes the charging station identified by the provided ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Charging station deleted successfully"),
      @ApiResponse(responseCode = "404", description = "Charging station not found")
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteStation(
      @Parameter(description = "ID of the charging station to delete") @PathVariable Long id) {
    try {
      chargingStationService.deleteStation(id);
      return ResponseEntity.noContent().build();
    } catch (NotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @Operation(summary = "Update a charging station by ID",
             description = "Updates the charging station identified by the provided ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Charging station updated successfully"),
      @ApiResponse(responseCode = "404", description = "Charging station not found"),
      @ApiResponse(responseCode = "400", description = "Invalid charging station data")
  })
  @PutMapping("/{id}")
  public ResponseEntity<ChargingStation> updateStation(
      @Parameter(description = "ID of the charging station to update") @PathVariable Long id,
      @RequestBody ChargingStation station) {
    try {
      ChargingStation updatedStation = chargingStationService.updateStation(id, station);
      return ResponseEntity.ok(updatedStation);
    } catch (NotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @Operation(summary = "Get all charging stations",
             description = "Retrieves all charging stations available in the system")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Charging stations retrieved successfully")
  })
  @GetMapping
  public ResponseEntity<List<ChargingStation>> getAllStations() {
    List<ChargingStation> stations = chargingStationService.getAllStations();
    return ResponseEntity.ok(stations);
  }
}
