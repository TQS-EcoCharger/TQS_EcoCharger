package pt.ua.tqs.ecocharger.ecocharger.service.interfaces;

import pt.ua.tqs.ecocharger.ecocharger.dto.ActiveSessionDTO;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingPoint;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingSession;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingStation;
import java.util.List;

public interface ChargingPointService {
  ChargingPoint createPoint(ChargingPoint point, ChargingStation station);

  List<ChargingPoint> getAllPoints();

  List<ChargingPoint> getAvailablePoints(ChargingStation station);

  List<ChargingPoint> getPointsByStationId(Long stationId);

  void deletePoint(Long id);
    
  ActiveSessionDTO getActiveSessionForPoint(Long pointId);

}
