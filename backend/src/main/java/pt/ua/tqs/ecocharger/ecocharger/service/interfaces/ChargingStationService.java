package pt.ua.tqs.ecocharger.ecocharger.service.interfaces;

import pt.ua.tqs.ecocharger.ecocharger.models.ChargingStation;
import java.util.List;

public interface ChargingStationService {
    ChargingStation createStation(ChargingStation station);
    List<ChargingStation> getAllStationsByCityName(String cityName);
    void deleteStation(Long id);
    List<ChargingStation> getAllStations();
}
