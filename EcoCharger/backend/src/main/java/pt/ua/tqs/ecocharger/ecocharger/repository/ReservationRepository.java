package pt.ua.tqs.ecocharger.ecocharger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pt.ua.tqs.ecocharger.ecocharger.models.Reservation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

  @Query(
      "SELECT COUNT(r) > 0 FROM Reservation r "
          + "WHERE r.chargingPoint.id = :chargingPointId "
          + "AND r.endTime > :start AND r.startTime < :end")
  boolean existsByChargingPointIdAndTimeOverlap(
      Long chargingPointId, LocalDateTime start, LocalDateTime end);

  List<Reservation> findByUserId(Long userId);

  List<Reservation> findByChargingPointIdAndEndTimeAfter(Long chargingPointId, LocalDateTime time);

  Optional<Reservation> findFirstByChargingPointIdAndStartTimeBeforeAndEndTimeAfter(
      Long chargingPointId, LocalDateTime start, LocalDateTime end);
}
