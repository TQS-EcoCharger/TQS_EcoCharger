package pt.ua.tqs.ecocharger.ecocharger.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pt.ua.tqs.ecocharger.ecocharger.dto.CreateChargingPointRequest;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingPoint;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingStation;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.ChargingPointService;
import pt.ua.tqs.ecocharger.ecocharger.utils.NotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/points")
public class ChargingPointController {

  private final ChargingPointService chargingPointService;

  public ChargingPointController(ChargingPointService chargingPointService) {
    this.chargingPointService = chargingPointService;
  }

  @PostMapping
  public ResponseEntity<ChargingPoint> createPoint(
      @RequestBody CreateChargingPointRequest request) {
    ChargingPoint saved =
        chargingPointService.createPoint(request.getPoint(), request.getStation());
    return ResponseEntity.ok(saved);
  }

  @GetMapping
  public ResponseEntity<List<ChargingPoint>> getAllPoints() {
    return ResponseEntity.ok(chargingPointService.getAllPoints());
  }

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

  @GetMapping("/station/{stationId}")
  public ResponseEntity<List<ChargingPoint>> getPointsByStationId(@PathVariable Long stationId) {
    return ResponseEntity.ok(chargingPointService.getPointsByStationId(stationId));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletePoint(@PathVariable Long id) {
    chargingPointService.deletePoint(id);
    return ResponseEntity.noContent().build();
  }
}
