package pt.ua.tqs.ecocharger.ecocharger.service.interfaces;

import pt.ua.tqs.ecocharger.ecocharger.models.ChargingPoint;

import java.util.List;

public interface ChargingPointService {
    ChargingPoint savePoint(ChargingPoint point);
    List<ChargingPoint> getAllPoints();
    List<ChargingPoint> getAvailablePoints();
    List<ChargingPoint> getPointsByStationId(Long stationId);
    void deletePoint(Long id);
}
