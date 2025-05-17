package pt.ua.tqs.ecocharger.ecocharger.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pt.ua.tqs.ecocharger.ecocharger.models.ChargingPoint;

@Repository
public interface ChargingPointRepository  extends JpaRepository<ChargingPoint, Long> {
    Optional<List<ChargingPoint>> findByAvailable(boolean available);
    Optional<List<ChargingPoint>> findByChargingStationId(Long stationId);
}