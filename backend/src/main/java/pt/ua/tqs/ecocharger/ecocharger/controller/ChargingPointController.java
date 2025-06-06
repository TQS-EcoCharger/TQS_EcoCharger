package pt.ua.tqs.ecocharger.ecocharger.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import pt.ua.tqs.ecocharger.ecocharger.dto.CreateChargingPointRequest;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingPoint;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingStation;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.ChargingPointService;
import pt.ua.tqs.ecocharger.ecocharger.utils.NotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/points")
@Tag(name = "Charging Points", description = "Endpoints for managing charging points")
public class ChargingPointController {

  private final ChargingPointService chargingPointService;

  public ChargingPointController(ChargingPointService chargingPointService) {
    this.chargingPointService = chargingPointService;
  }

  @Operation(
      summary = "Create a new charging point",
      description = "Creates a new charging point linked to a charging station")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Charging point created successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid input data")
  })
  @PostMapping
  public ResponseEntity<ChargingPoint> createPoint(
      @RequestBody CreateChargingPointRequest request) {
    ChargingPoint saved =
        chargingPointService.createPoint(request.getPoint(), request.getStation());
    return ResponseEntity.ok(saved);
  }

  @Operation(
      summary = "Get all charging points",
      description = "Retrieves a list of all charging points in the system")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "List of charging points retrieved successfully")
  })
  @GetMapping
  public ResponseEntity<List<ChargingPoint>> getAllPoints() {
    return ResponseEntity.ok(chargingPointService.getAllPoints());
  }

  @Operation(
      summary = "Update an existing charging point",
      description = "Updates the details of a charging point identified by its ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Charging point updated successfully"),
      @ApiResponse(responseCode = "404", description = "Charging point not found")
  })
  @PutMapping("/{id}")
  public ResponseEntity<ChargingPoint> updatePoint(
      @Parameter(description = "ID of the charging point to update") @PathVariable Long id,
      @RequestBody ChargingPoint point) {
    try {
      ChargingPoint updatedPoint = chargingPointService.updatePoint(id, point);
      return ResponseEntity.ok(updatedPoint);
    } catch (NotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @Operation(
      summary = "Get available charging points in a station",
      description = "Returns a list of available charging points for a given charging station")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Available charging points retrieved successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid charging station data")
  })
  @GetMapping("/available")
  public ResponseEntity<List<ChargingPoint>> getAvailablePoints(
      @RequestBody ChargingStation station) {
    return ResponseEntity.ok(chargingPointService.getAvailablePoints(station));
  }

  @Operation(
      summary = "Get charging points by station ID",
      description = "Retrieves all charging points associated with a specific charging station ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Charging points retrieved successfully"),
      @ApiResponse(responseCode = "404", description = "Charging station not found")
  })
  @GetMapping("/station/{stationId}")
  public ResponseEntity<List<ChargingPoint>> getPointsByStationId(
      @Parameter(description = "ID of the charging station") @PathVariable Long stationId) {
    return ResponseEntity.ok(chargingPointService.getPointsByStationId(stationId));
  }

  @Operation(
      summary = "Delete a charging point by its ID",
      description = "Deletes the charging point identified by the provided ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Charging point deleted successfully"),
      @ApiResponse(responseCode = "404", description = "Charging point not found")
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletePoint(
      @Parameter(description = "ID of the charging point to delete") @PathVariable Long id) {
    chargingPointService.deletePoint(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(
      summary = "Get active session for a charging point",
      description = "Retrieves the active charging session associated with the charging point")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Active session retrieved successfully"),
      @ApiResponse(responseCode = "404", description = "Charging point or active session not found")
  })
  @GetMapping("/{id}/active-session")
  public ResponseEntity<?> getActiveSessionForPoint(
      @Parameter(description = "ID of the charging point") @PathVariable Long id) {
    return ResponseEntity.ok(chargingPointService.getActiveSessionForPoint(id));
  }
}
