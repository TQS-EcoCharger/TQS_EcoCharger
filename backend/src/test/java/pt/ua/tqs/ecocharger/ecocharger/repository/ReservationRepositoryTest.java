package pt.ua.tqs.ecocharger.ecocharger.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import app.getxray.xray.junit.customjunitxml.annotations.Requirement;
import pt.ua.tqs.ecocharger.ecocharger.models.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ReservationRepositoryTest {

  @Autowired private ReservationRepository reservationRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private ChargingStationRepository stationRepository;
  @Autowired private ChargingPointRepository pointRepository;

  private User createUser(String email) {
    User user = new Driver(null, email, "password", "Test User", true);
    return userRepository.save(user);
  }

  private ChargingPoint createChargingPoint() {
    ChargingStation station = new ChargingStation("Aveiro", "Rua X", 40.0, -8.0, "PT", "Portugal");
    station = stationRepository.save(station);

    ChargingPoint point = new ChargingPoint();
    point.setChargingStation(station);
    point.setAvailable(true);
    point.setBrand("Efacec");
    point.setChargingRateKWhPerMinute(1.0);
    point.setPricePerKWh(0.25);
    point.setPricePerMinute(0.10);

    return pointRepository.save(point);
  }

  private Reservation createReservation(
      User user,
      ChargingPoint point,
      LocalDateTime start,
      LocalDateTime end,
      ReservationStatus status) {
    Reservation reservation = new Reservation();
    reservation.setUser(user);
    reservation.setChargingPoint(point);
    reservation.setStartTime(start);
    reservation.setEndTime(end);
    reservation.setStatus(status);
    return reservationRepository.save(reservation);
  }

  @Test
  @Requirement("ET-30")
  @DisplayName("Test existsByChargingPointIdAndTimeOverlap - overlap exists")
  void testExistsByChargingPointIdAndTimeOverlap_true() {
    User user = createUser("overlap@test.com");
    ChargingPoint point = createChargingPoint();

    LocalDateTime now = LocalDateTime.now();
    createReservation(
        user, point, now.plusMinutes(10), now.plusMinutes(30), ReservationStatus.TO_BE_USED);

    boolean exists =
        reservationRepository.existsByChargingPointIdAndTimeOverlap(
            point.getId(), now.plusMinutes(20), now.plusMinutes(40));

    assertThat(exists).isTrue();
  }

  @Test
  @Requirement("ET-30")
  @DisplayName("Test existsByChargingPointIdAndTimeOverlap - no overlap")
  void testExistsByChargingPointIdAndTimeOverlap_false() {
    User user = createUser("noverlap@test.com");
    ChargingPoint point = createChargingPoint();

    LocalDateTime now = LocalDateTime.now();
    createReservation(
        user, point, now.plusMinutes(10), now.plusMinutes(20), ReservationStatus.TO_BE_USED);

    boolean exists =
        reservationRepository.existsByChargingPointIdAndTimeOverlap(
            point.getId(), now.plusMinutes(21), now.plusMinutes(30));

    assertThat(exists).isFalse();
  }

  @Test
  @Requirement("ET-30")
  @DisplayName("Find reservations by user ID")
  void testFindByUserId() {
    User user = createUser("user1@test.com");
    ChargingPoint point = createChargingPoint();

    createReservation(
        user,
        point,
        LocalDateTime.now(),
        LocalDateTime.now().plusMinutes(20),
        ReservationStatus.TO_BE_USED);

    List<Reservation> reservations = reservationRepository.findByUserId(user.getId());
    assertThat(reservations).hasSize(1);
    assertThat(reservations.get(0).getUser().getId()).isEqualTo(user.getId());
  }

  @Test
  @Requirement("ET-30")
  @DisplayName("Find future reservations by charging point ID")
  void testFindByChargingPointIdAndEndTimeAfter() {
    User user = createUser("future@test.com");
    ChargingPoint point = createChargingPoint();

    LocalDateTime now = LocalDateTime.now();
    createReservation(
        user, point, now.plusMinutes(5), now.plusMinutes(25), ReservationStatus.TO_BE_USED);

    List<Reservation> futureRes =
        reservationRepository.findByChargingPointIdAndEndTimeAfter(point.getId(), now);

    assertThat(futureRes).isNotEmpty();
  }

  @Test
  @Requirement("ET-30")
  @DisplayName("Find first reservation by point and within interval")
  void testFindFirstByChargingPointIdAndStartTimeBeforeAndEndTimeAfter() {
    User user = createUser("interval@test.com");
    ChargingPoint point = createChargingPoint();

    LocalDateTime now = LocalDateTime.now();
    createReservation(
        user, point, now.plusMinutes(5), now.plusMinutes(30), ReservationStatus.TO_BE_USED);

    Optional<Reservation> res =
        reservationRepository.findFirstByChargingPointIdAndStartTimeBeforeAndEndTimeAfter(
            point.getId(), now.plusMinutes(10), now.plusMinutes(20));

    assertThat(res).isPresent();
    assertThat(res.get().getChargingPoint().getId()).isEqualTo(point.getId());
  }
}
