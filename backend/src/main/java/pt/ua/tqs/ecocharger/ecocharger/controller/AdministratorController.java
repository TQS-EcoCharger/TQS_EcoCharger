package pt.ua.tqs.ecocharger.ecocharger.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
  @ApiResponse(
      responseCode = "200",
      description = "Administrator created successfully",
      content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = Administrator.class)))
  @PostMapping
  public ResponseEntity<Administrator> createAdministrator(@RequestBody Administrator admin) {
    return ResponseEntity.ok(
        administratorService.createAdministrator(
            admin.getEmail(), admin.getPassword(), admin.getName()));
  }

  @Operation(summary = "Update a charging station")
  @ApiResponse(
      responseCode = "200",
      description = "Charging station updated successfully",
      content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ChargingStation.class)))
  @PutMapping("/stations/{stationId}")
  public ResponseEntity<ChargingStation> updateStation(
      @Parameter(description = "ID of the charging station to update") @PathVariable Long stationId,
      @RequestBody ChargingStation station) {
    station.setId(stationId);
    return ResponseEntity.ok(administratorService.updateChargingStation(station));
  }
  

  @Operation(summary = "Update a charging point")
  @ApiResponse(
      responseCode = "200",
      description = "Charging point updated successfully",
      content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ChargingPoint.class)))
  @PutMapping("/stations/{stationId}/points/{pointId}")
  public ResponseEntity<ChargingPoint> updatePoint(
      @RequestBody ChargingPoint point,
      @Parameter(description = "ID of the parent charging station") @PathVariable Long stationId,
      @Parameter(description = "ID of the charging point to update") @PathVariable Long pointId) {
    ChargingStation station = new ChargingStation();
    station.setId(stationId);
    point.setChargingStation(station);
    return ResponseEntity.ok(administratorService.updateChargingPoint(point, pointId));
  }
  

  @Operation(summary = "Delete a charging station")
  @ApiResponse(
      responseCode = "200",
      description = "Charging station deleted successfully",
      content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ChargingStation.class)))
  @DeleteMapping("/stations/{stationId}")
  public ResponseEntity<ChargingStation> deleteStation(
      @Parameter(description = "ID of the charging station to delete") @PathVariable Long stationId) {
    return ResponseEntity.ok(administratorService.deleteChargingStation(stationId));
  }
  
}
