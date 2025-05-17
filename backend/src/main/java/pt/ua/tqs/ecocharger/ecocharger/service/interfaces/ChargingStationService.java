package pt.ua.tqs.ecocharger.ecocharger.service.interfaces;

import pt.ua.tqs.ecocharger.ecocharger.models.ChargingStation;

import java.util.List;
import java.util.Optional;

public interface ChargingStationService {
    ChargingStation saveStation(ChargingStation station);
    List<ChargingStation> getAllStations();
    Optional<ChargingStation> getStationByCityName(String cityName);
    void deleteStation(Long id);
}
