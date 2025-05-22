package pt.ua.tqs.ecocharger.ecocharger.service;

import java.util.List;
import java.util.Optional;

import pt.ua.tqs.ecocharger.ecocharger.models.ChargingPoint;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingStation;
import pt.ua.tqs.ecocharger.ecocharger.repository.ChargingPointRepository;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.ChargingPointService;
import org.springframework.stereotype.Service;

@Service
public class ChargingPointServiceImpl implements ChargingPointService {

  private ChargingPointRepository chargingPointRepository;

  public ChargingPointServiceImpl(ChargingPointRepository chargingPointRepository) {
    this.chargingPointRepository = chargingPointRepository;
  }

  @Override
  public ChargingPoint createPoint(ChargingPoint point, ChargingStation station) {
    Optional<List<ChargingPoint>> points =
        chargingPointRepository.findByChargingStationId(station.getId());
    if (points.isPresent()) {
      for (ChargingPoint existingPoint : points.get()) {
        if (existingPoint.getId().equals(point.getId())) {
          return existingPoint;
        }
      }
    }
    point.setChargingStation(station);
    return chargingPointRepository.save(point);
  }

  @Override
  public List<ChargingPoint> getAllPoints() {
    List<ChargingPoint> points = chargingPointRepository.findAll();
    return points.stream().filter(point -> point.getId() != null).toList();
  }

  @Override
  public List<ChargingPoint> getAvailablePoints(ChargingStation station) {
    Optional<List<ChargingPoint>> points =
        chargingPointRepository.findAvailablePointsByChargingStation(station);
    return points.orElseThrow(() -> new RuntimeException("No available points found"));
  }

  @Override
  public void deletePoint(Long id) {
    Optional<ChargingPoint> point = chargingPointRepository.findById(id);
    if (point.isPresent()) {
      chargingPointRepository.delete(point.get());
    } else {
      throw new RuntimeException("Point not found");
    }
  }

  @Override
  public List<ChargingPoint> getPointsByStationId(Long stationId) {
    Optional<List<ChargingPoint>> points =
        chargingPointRepository.findByChargingStationId(stationId);
    return points.orElseThrow(() -> new RuntimeException("No points found for this station"));
  }
}
