package pt.ua.tqs.ecocharger.ecocharger.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingPoint;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingStation;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.ChargingPointService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/points")
public class ChargingPointController {

    private final ChargingPointService chargingPointService;

    public ChargingPointController(ChargingPointService chargingPointService) {
        this.chargingPointService = chargingPointService;
    }

    @PostMapping
    public ResponseEntity<ChargingPoint> createPoint(@RequestBody ChargingPoint point, @RequestBody ChargingStation station) {
        ChargingPoint saved = chargingPointService.createPoint(point, station);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<ChargingPoint>> getAllPoints() {
        return ResponseEntity.ok(chargingPointService.getAllPoints());
    }

    @GetMapping("/available")
    public ResponseEntity<List<ChargingPoint>> getAvailablePoints(@RequestBody ChargingStation station) {
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
