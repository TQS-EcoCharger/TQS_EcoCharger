package pt.ua.tqs.ecocharger.ecocharger.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pt.ua.tqs.ecocharger.ecocharger.models.*;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class OTPCodeRepositoryTest {

  @Autowired
  private OTPCodeRepository otpCodeRepository;

  @Autowired
  private ReservationRepository reservationRepository;

  @Autowired
  private ChargingPointRepository chargingPointRepository;

  @Autowired
  private ChargingStationRepository chargingStationRepository;

  @Autowired
  private UserRepository userRepository;

  private Reservation createReservation(User user, ChargingPoint point) {
    Reservation reservation = new Reservation();
    reservation.setUser(user);
    reservation.setChargingPoint(point);
    reservation.setStartTime(LocalDateTime.now().plusMinutes(5));
    reservation.setEndTime(LocalDateTime.now().plusMinutes(45));
    reservation.setStatus(ReservationStatus.TO_BE_USED);
    return reservationRepository.save(reservation);
  }

  private ChargingPoint createPoint() {
    ChargingStation station = new ChargingStation(
        "Test City", "Test Street", 1.23, 4.56, "1234-123", "PT");
    chargingStationRepository.save(station);

    ChargingPoint point = new ChargingPoint();
    point.setChargingStation(station);
    point.setAvailable(true);
    point.setBrand("TestBrand");
    point.setPricePerKWh(0.25);
    point.setChargingRateKWhPerMinute(2.0);
    point.setPricePerMinute(0.03);
    return chargingPointRepository.save(point);
  }

  private User createUser() {
    return userRepository.save(new User(null, "test@example.com", "password", "TestUser", true));
  }

  @Test
  @DisplayName("Find OTP by code and reservation")
  void testFindByCodeAndReservation() {
    User user = createUser();
    ChargingPoint point = createPoint();
    Reservation reservation = createReservation(user, point);

    OTPCode otp = new OTPCode();
    otp.setCode("123456");
    otp.setReservation(reservation);
    otp.setExpirationTime(LocalDateTime.now().plusMinutes(10));
    otpCodeRepository.save(otp);

    Optional<OTPCode> found = otpCodeRepository.findByCodeAndReservation("123456", reservation);

    assertThat(found).isPresent();
    assertThat(found.get().getCode()).isEqualTo("123456");
    assertThat(found.get().getReservation().getId()).isEqualTo(reservation.getId());
  }

  @Test
  @DisplayName("Find OTP by reservation")
  void testFindByReservation() {
    User user = createUser();
    ChargingPoint point = createPoint();
    Reservation reservation = createReservation(user, point);

    OTPCode otp = new OTPCode();
    otp.setCode("654321");
    otp.setExpirationTime(LocalDateTime.now().plusMinutes(10));
    otp.setReservation(reservation);
    otpCodeRepository.save(otp);

    Optional<OTPCode> found = otpCodeRepository.findByReservation(reservation);

    assertThat(found).isPresent();
    assertThat(found.get().getCode()).isEqualTo("654321");
    assertThat(found.get().getReservation().getId()).isEqualTo(reservation.getId());
  }

  @Test
  @DisplayName("Return empty when OTP not found")
  void testFindByCodeAndReservation_NotFound() {
    User user = createUser();
    ChargingPoint point = createPoint();
    Reservation reservation = createReservation(user, point);

    Optional<OTPCode> found = otpCodeRepository.findByCodeAndReservation("000000", reservation);

    assertThat(found).isEmpty();
  }
}
