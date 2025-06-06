package pt.ua.tqs.ecocharger.ecocharger.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import pt.ua.tqs.ecocharger.ecocharger.dto.ActiveSessionDTO;
import pt.ua.tqs.ecocharger.ecocharger.models.Car;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingPoint;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingSession;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingStation;
import pt.ua.tqs.ecocharger.ecocharger.repository.ChargingPointRepository;
import pt.ua.tqs.ecocharger.ecocharger.repository.ChargingSessionRepository;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.ChargingPointService;
import pt.ua.tqs.ecocharger.ecocharger.utils.NotFoundException;

import org.springframework.stereotype.Service;

@Service
public class ChargingPointServiceImpl implements ChargingPointService {

  private final ChargingPointRepository chargingPointRepository;
  private final ChargingSessionRepository chargingSessionRepository;

  public ChargingPointServiceImpl(
      ChargingPointRepository pointRepo, ChargingSessionRepository sessionRepo) {
    this.chargingPointRepository = pointRepo;
    this.chargingSessionRepository = sessionRepo;
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
  public ChargingPoint updatePoint(Long id, ChargingPoint point) {
    Optional<ChargingPoint> existingPoint = chargingPointRepository.findById(id);
    if (existingPoint.isPresent()) {
      ChargingPoint updatedPoint = existingPoint.get();
      updatedPoint.setChargingStation(point.getChargingStation());
      updatedPoint.setAvailable(point.isAvailable());
      updatedPoint.setBrand(point.getBrand());
      updatedPoint.setConnectors(point.getConnectors());
      updatedPoint.setPricePerKWh(point.getPricePerKWh());
      updatedPoint.setPricePerMinute(point.getPricePerMinute());
      return chargingPointRepository.save(updatedPoint);
    } else {
      throw new NotFoundException("Point not found");
    }
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

  @Override
  public ActiveSessionDTO getActiveSessionForPoint(Long pointId) {
    ChargingPoint point =
        chargingPointRepository
            .findById(pointId)
            .orElseThrow(() -> new IllegalArgumentException("Charging point not found"));

    ChargingSession session =
        chargingSessionRepository
            .findByChargingPointAndEndTimeIsNull(point)
            .orElseThrow(() -> new IllegalStateException("No active session on this point"));

    long durationMinutes =
        java.time.Duration.between(session.getStartTime(), LocalDateTime.now()).toMinutes();

    Car car = session.getCar();
    double initialBatteryLevel = session.getInitialBatteryLevel();
    double batteryCapacity = car.getBatteryCapacity();

    double chargingRate = point.getChargingRateKWhPerMinute();
    double energyDelivered = durationMinutes * chargingRate;
    double estimatedBatteryLevel = Math.min(initialBatteryLevel + energyDelivered, batteryCapacity);
    double batteryPercentage = (estimatedBatteryLevel / batteryCapacity) * 100.0;

    double costPerKWh = point.getPricePerKWh() != null ? point.getPricePerKWh() : 0.0;
    double costPerMinute = point.getPricePerMinute() != null ? point.getPricePerMinute() : 0.0;
    double totalCost = (energyDelivered * costPerKWh) + (durationMinutes * costPerMinute);

    return new ActiveSessionDTO(
        session.getId(),
        point.getId(),
        car.getId(),
        car.getName(),
        durationMinutes,
        batteryPercentage,
        energyDelivered,
        totalCost);
  }
}
