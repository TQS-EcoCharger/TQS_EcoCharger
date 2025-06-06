package pt.ua.tqs.ecocharger.ecocharger.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

  @Operation(summary = "Create a new charging point")
  @PostMapping
  public ResponseEntity<ChargingPoint> createPoint(
      @RequestBody CreateChargingPointRequest request) {
    ChargingPoint saved =
        chargingPointService.createPoint(request.getPoint(), request.getStation());
    return ResponseEntity.ok(saved);
  }

  @Operation(summary = "Get all charging points")
  @GetMapping
  public ResponseEntity<List<ChargingPoint>> getAllPoints() {
    return ResponseEntity.ok(chargingPointService.getAllPoints());
  }

  @Operation(summary = "Get available charging points in a station")
  @PutMapping("/{id}")
  public ResponseEntity<ChargingPoint> updatePoint(
      @PathVariable Long id, @RequestBody ChargingPoint point) {
    try {
      ChargingPoint updatedPoint = chargingPointService.updatePoint(id, point);
      return ResponseEntity.ok(updatedPoint);
    } catch (NotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/available")
  public ResponseEntity<List<ChargingPoint>> getAvailablePoints(
      @RequestBody ChargingStation station) {
    return ResponseEntity.ok(chargingPointService.getAvailablePoints(station));
  }

  @Operation(summary = "Get charging points by station ID")
  @GetMapping("/station/{stationId}")
  public ResponseEntity<List<ChargingPoint>> getPointsByStationId(
      @Parameter(description = "ID of the charging station") @PathVariable Long stationId) {
    return ResponseEntity.ok(chargingPointService.getPointsByStationId(stationId));
  }

  @Operation(summary = "Delete a charging point by its ID")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletePoint(
      @Parameter(description = "ID of the charging point to delete") @PathVariable Long id) {
    chargingPointService.deletePoint(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{id}/active-session")
  public ResponseEntity<?> getActiveSessionForPoint(@PathVariable Long id) {
    return ResponseEntity.ok(chargingPointService.getActiveSessionForPoint(id));
  }
}
