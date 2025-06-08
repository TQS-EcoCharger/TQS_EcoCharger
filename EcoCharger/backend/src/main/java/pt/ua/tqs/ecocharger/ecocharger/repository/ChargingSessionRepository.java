package pt.ua.tqs.ecocharger.ecocharger.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pt.ua.tqs.ecocharger.ecocharger.models.ChargingPoint;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingSession;

import java.util.List;
import java.util.Optional;

public interface ChargingSessionRepository extends JpaRepository<ChargingSession, Long> {
  List<ChargingSession> findByUserId(Long userId);

  List<ChargingSession> findByChargingPointId(Long chargingPointId);

  Optional<ChargingSession> findByChargingPointAndEndTimeIsNull(ChargingPoint chargingPoint);
}
