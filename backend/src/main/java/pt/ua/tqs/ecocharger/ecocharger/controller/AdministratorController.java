package pt.ua.tqs.ecocharger.ecocharger.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    @Operation(
        summary = "Create a new administrator",
        description = "Registers a new administrator with email, password, and name."
    )
    @PostMapping
    public ResponseEntity<Administrator> createAdministrator(
        @RequestBody Administrator admin
    ) {
        return ResponseEntity.ok(
            administratorService.createAdministrator(
                admin.getEmail(), admin.getPassword(), admin.getName()
            )
        );
    }

    @Operation(
        summary = "Update a charging station",
        description = "Updates an existing charging station identified by its ID."
    )
    @PutMapping("/stations/{stationId}")
    public ResponseEntity<ChargingStation> updateStation(
        @Parameter(description = "ID of the charging station to update")
        @PathVariable Long stationId,
        @RequestBody ChargingStation station
    ) {
        station.setId(stationId);
        return ResponseEntity.ok(administratorService.updateChargingStation(station));
    }

    @Operation(
        summary = "Update a charging point",
        description = "Updates a specific charging point within a station."
    )
    @PutMapping("/stations/{stationId}/points/{pointId}")
    public ResponseEntity<ChargingPoint> updatePoint(
        @RequestBody ChargingPoint point,
        @Parameter(description = "ID of the parent charging station")
        @PathVariable Long stationId,
        @Parameter(description = "ID of the charging point to update")
        @PathVariable Long pointId
    ) {
        ChargingStation station = new ChargingStation();
        station.setId(stationId);
        point.setChargingStation(station);
        return ResponseEntity.ok(administratorService.updateChargingPoint(point, pointId));
    }

    @Operation(
        summary = "Delete a charging station",
        description = "Deletes a charging station by ID and removes its related charging points."
    )
    @DeleteMapping("/stations/{stationId}")
    public ResponseEntity<ChargingStation> deleteStation(
        @Parameter(description = "ID of the charging station to delete")
        @PathVariable Long stationId
    ) {
        return ResponseEntity.ok(administratorService.deleteChargingStation(stationId));
    }
}
