/*
 * package pt.ua.tqs.ecocharger.ecocharger.repository;
 *
 * import org.junit.jupiter.api.BeforeEach;
 * import org.junit.jupiter.api.Test;
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
 *
 * import app.getxray.xray.junit.customjunitxml.annotations.Requirement;
 * import pt.ua.tqs.ecocharger.ecocharger.models.*;
 *
 * import java.time.LocalDateTime;
 * import java.util.Collections;
 * import java.util.List;
 *
 * import static org.assertj.core.api.Assertions.assertThat;
 *
 * @DataJpaTest
 * public class ReservationRepositoryTest {
 *
 * @Autowired private ReservationRepository reservationRepository;
 *
 * @Autowired private UserRepository userRepository;
 *
 * @Autowired private ChargingStationRepository chargingStationRepository;
 *
 * @Autowired private ChargingPointRepository chargingPointRepository;
 *
 * private User user;
 * private ChargingPoint chargingPoint;
 *
 * @BeforeEach
 * void setup() {
 * // Create and save User
 * user = new User();
 * user.setEmail("test@example.com");
 * user.setPassword("password");
 * user = userRepository.save(user);
 *
 * // Create and save ChargingStation
 * ChargingStation station =
 * new ChargingStation(
 * "Aveiro", "123 Main St", 40.6405, -8.6538, "Main St", "PT", "Portugal",
 * "Electric");
 * station = chargingStationRepository.save(station);
 *
 * // Create and save ChargingPoint
 * chargingPoint = new ChargingPoint(station, true, "Tesla",
 * Collections.emptyList());
 * chargingPoint.setPricePerKWh(0.35);
 * chargingPoint.setPricePerMinute(0.10);
 * chargingPoint = chargingPointRepository.save(chargingPoint);
 * }
 *
 * @Test
 *
 * @Requirement("ET-30")
 * void
 * testExistsByChargingPointIdAndTimeOverlap_shouldReturnTrueWhenOverlapping() {
 * LocalDateTime now = LocalDateTime.now();
 *
 * Reservation reservation =
 * new Reservation(
 * null,
 * user,
 * chargingPoint,
 * now.plusHours(1),
 * now.plusHours(2),
 * ReservationStatus.TO_BE_USED);
 * reservationRepository.save(reservation);
 *
 * boolean exists =
 * reservationRepository.existsByChargingPointIdAndTimeOverlap(
 * chargingPoint.getId(), now.plusMinutes(90), now.plusHours(3));
 *
 * assertThat(exists).isTrue();
 * }
 *
 * @Test
 *
 * @Requirement("ET-30")
 * void
 * testExistsByChargingPointIdAndTimeOverlap_shouldReturnFalseWhenNotOverlapping
 * () {
 * LocalDateTime now = LocalDateTime.now();
 *
 * Reservation reservation =
 * new Reservation(
 * null,
 * user,
 * chargingPoint,
 * now.plusHours(1),
 * now.plusHours(2),
 * ReservationStatus.TO_BE_USED);
 * reservationRepository.save(reservation);
 *
 * boolean exists =
 * reservationRepository.existsByChargingPointIdAndTimeOverlap(
 * chargingPoint.getId(), now.plusHours(2), now.plusHours(3));
 *
 * assertThat(exists).isFalse();
 * }
 *
 * @Test
 *
 * @Requirement("ET-30")
 * void testFindByUserId_shouldReturnReservations() {
 * Reservation reservation =
 * new Reservation(
 * null,
 * user,
 * chargingPoint,
 * LocalDateTime.now(),
 * LocalDateTime.now().plusHours(1),
 * ReservationStatus.TO_BE_USED);
 * reservationRepository.save(reservation);
 *
 * List<Reservation> results = reservationRepository.findByUserId(user.getId());
 *
 * assertThat(results).hasSize(1);
 * assertThat(results.get(0).getUser().getId()).isEqualTo(user.getId());
 * }
 *
 * @Test
 *
 * @Requirement("ET-30")
 * void
 * testFindByChargingPointIdAndEndTimeAfter_shouldReturnFutureReservations() {
 * LocalDateTime now = LocalDateTime.now();
 *
 * Reservation reservation =
 * new Reservation(
 * null,
 * user,
 * chargingPoint,
 * now.plusMinutes(30),
 * now.plusHours(1),
 * ReservationStatus.TO_BE_USED);
 * reservationRepository.save(reservation);
 *
 * List<Reservation> results =
 * reservationRepository.findByChargingPointIdAndEndTimeAfter(chargingPoint.
 * getId(), now);
 *
 * assertThat(results).hasSize(1);
 * assertThat(results.get(0).getChargingPoint().getId()).isEqualTo(chargingPoint
 * .getId());
 * }
 * }
 *
 */
