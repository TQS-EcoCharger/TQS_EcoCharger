package pt.ua.tqs.ecocharger.ecocharger.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pt.ua.tqs.ecocharger.ecocharger.models.ChargingStation;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.ChargingStationService;
import pt.ua.tqs.ecocharger.ecocharger.utils.NotFoundException;

@RestController
@RequestMapping("/api/v1/chargingStations")
public class ChargingStationController {

  private final ChargingStationService chargingStationService;

  public ChargingStationController(ChargingStationService chargingStationService) {
    this.chargingStationService = chargingStationService;
  }

  @PostMapping
  public ResponseEntity<ChargingStation> createStation(@RequestBody ChargingStation station) {
    ChargingStation savedStation = chargingStationService.createStation(station);
    return ResponseEntity.ok(savedStation);
  }

  @GetMapping("/city/{cityName}")
  public ResponseEntity<List<ChargingStation>> getStationsByCity(@PathVariable String cityName) {
    List<ChargingStation> stations = chargingStationService.getAllStationsByCityName(cityName);
    return ResponseEntity.ok(stations);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
    chargingStationService.deleteStation(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<ChargingStation> updateStation(
      @PathVariable Long id, @RequestBody ChargingStation station) {
        try {
          ChargingStation updatedStation = chargingStationService.updateStation(id, station);
          return ResponseEntity.ok(updatedStation);
        }
        catch (NotFoundException e) {
          return ResponseEntity.notFound().build();
        }    
  }

  @GetMapping
  public ResponseEntity<List<ChargingStation>> getAllStations() {
    List<ChargingStation> stations = chargingStationService.getAllStations();
    return ResponseEntity.ok(stations);
  }
}
