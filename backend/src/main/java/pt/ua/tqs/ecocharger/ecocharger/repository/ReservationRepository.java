package pt.ua.tqs.ecocharger.ecocharger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pt.ua.tqs.ecocharger.ecocharger.models.Reservation;

import java.time.LocalDateTime;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT COUNT(r) > 0 FROM Reservation r " +
            "WHERE r.chargingPoint.id = :chargingPointId " +
            "AND r.endTime > :start AND r.startTime < :end")
    boolean existsByChargingPointIdAndTimeOverlap(Long chargingPointId, LocalDateTime start, LocalDateTime end);
}
