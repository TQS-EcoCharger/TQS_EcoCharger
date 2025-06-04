package pt.ua.tqs.ecocharger.ecocharger.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pt.ua.tqs.ecocharger.ecocharger.models.Administrator;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingPoint;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingStation;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.AdministratorService;

@RestController
@RequestMapping("/api/v1/admin")
@Tag(name = "Administrator", description = "Management of administrators and charging stations")
public class AdministratorController {

  private final AdministratorService administratorService;

  public AdministratorController(AdministratorService administratorService) {
    this.administratorService = administratorService;
  }

  @Operation(summary = "Create a new administrator")
  @PostMapping
  public ResponseEntity<Administrator> createAdministrator(@RequestBody Administrator admin) {
    return ResponseEntity.ok(
        administratorService.createAdministrator(
            admin.getEmail(), admin.getPassword(), admin.getName()));
  }

  @Operation(summary = "Update a charging station")
  @PutMapping("/stations/{stationId}")
  public ResponseEntity<ChargingStation> updateStation(
      @PathVariable Long stationId, @RequestBody ChargingStation station) {
    station.setId(stationId);
    return ResponseEntity.ok(administratorService.updateChargingStation(station));
  }

  @Operation(summary = "Update a charging point")
  @PutMapping("/stations/{stationId}/points/{pointId}")
  public ResponseEntity<ChargingPoint> updatePoint(
      @RequestBody ChargingPoint point, @PathVariable Long stationId, @PathVariable Long pointId) {
    ChargingStation station = new ChargingStation();
    station.setId(stationId);
    point.setChargingStation(station);
    return ResponseEntity.ok(administratorService.updateChargingPoint(point, pointId));
  }

  @Operation(summary = "Delete a charging station")
  @DeleteMapping("/stations/{stationId}")
  public ResponseEntity<ChargingStation> deleteStation(@PathVariable Long stationId) {
    return ResponseEntity.ok(administratorService.deleteChargingStation(stationId));
  }
}
