package pt.ua.tqs.ecocharger.ecocharger.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import pt.ua.tqs.ecocharger.ecocharger.models.ChargingPoint;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingStation;

@Repository
public interface ChargingPointRepository extends JpaRepository<ChargingPoint, Long> {
  Optional<List<ChargingPoint>> findByAvailable(boolean available);

  Optional<List<ChargingPoint>> findByChargingStationId(Long stationId);

  @Query("SELECT cp FROM ChargingPoint cp WHERE cp.chargingStation = ?1 AND cp.available = true")
  Optional<List<ChargingPoint>> findAvailablePointsByChargingStation(ChargingStation stationId);
}
