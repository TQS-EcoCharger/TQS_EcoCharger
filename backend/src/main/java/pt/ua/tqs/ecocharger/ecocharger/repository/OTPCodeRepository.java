package pt.ua.tqs.ecocharger.ecocharger.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pt.ua.tqs.ecocharger.ecocharger.models.OTPCode;
import pt.ua.tqs.ecocharger.ecocharger.models.Reservation;

public interface OTPCodeRepository extends JpaRepository<OTPCode, Long> {
  Optional<OTPCode> findByCodeAndReservation(String code, Reservation reservation);
  Optional<OTPCode> findByReservation(Reservation reservation);

}
