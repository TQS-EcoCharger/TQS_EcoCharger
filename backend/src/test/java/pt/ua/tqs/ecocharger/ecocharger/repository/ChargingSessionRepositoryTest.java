package pt.ua.tqs.ecocharger.ecocharger.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pt.ua.tqs.ecocharger.ecocharger.models.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ChargingSessionRepositoryTest {

  @Autowired private ChargingSessionRepository chargingSessionRepository;
  @Autowired private ChargingPointRepository chargingPointRepository;
  @Autowired private ChargingStationRepository chargingStationRepository;
  @Autowired private CarRepository carRepository;
  @Autowired private UserRepository userRepository;

  private ChargingStation createStation(String suffix) {
    String randomId = UUID.randomUUID().toString().substring(0, 8);
    ChargingStation station =
        new ChargingStation(
            "Aveiro",
            "Rua A " + suffix,
            40.0 + Math.random(),
            -8.0 + Math.random(),
            "PT",
            "Portugal");
    return chargingStationRepository.save(station);
  }

  private ChargingPoint createPoint(String brand) {
    ChargingStation station = createStation(UUID.randomUUID().toString());
    ChargingPoint point = new ChargingPoint();
    point.setChargingStation(station);
    point.setAvailable(true);
    point.setBrand(brand);
    point.setChargingRateKWhPerMinute(1.5);
    point.setPricePerKWh(0.5);
    point.setPricePerMinute(0.1);
    return chargingPointRepository.save(point);
  }

  private Car createCar() {
    Car car =
        new Car(null, "Tesla", "Tesla", "Model 3", 2022, "AB-13-19", 75.0, 30.0, 10000.0, 15.0);
    return carRepository.save(car);
  }

  private Driver createDriver() {
    Driver driver =
        new Driver(null, UUID.randomUUID().toString() + "@example.com", "pass", "Driver", true);
    return (Driver) userRepository.save(driver);
  }

  @Test
  @DisplayName("Find sessions by user ID")
  void testFindByUserId() {
    Driver driver = createDriver();
    Car car = createCar();
    ChargingPoint point = createPoint("Tesla");

    ChargingSession session = new ChargingSession();
    session.setUser(driver);
    session.setCar(car);
    session.setStatus(ChargingStatus.IN_PROGRESS);
    session.setChargingPoint(point);
    session.setStartTime(LocalDateTime.now());
    session.setInitialBatteryLevel(30.0);
    chargingSessionRepository.save(session);

    List<ChargingSession> result = chargingSessionRepository.findByUserId(driver.getId());

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getUser().getId()).isEqualTo(driver.getId());
  }

  @Test
  @DisplayName("Find sessions by charging point ID")
  void testFindByChargingPointId() {
    Driver driver = createDriver();
    Car car = createCar();
    ChargingPoint point = createPoint("Nissan");

    ChargingSession session = new ChargingSession();
    session.setUser(driver);
    session.setCar(car);
    session.setChargingPoint(point);
    session.setStartTime(LocalDateTime.now());
    session.setInitialBatteryLevel(50.0);
    session.setStatus(ChargingStatus.IN_PROGRESS);
    chargingSessionRepository.save(session);

    List<ChargingSession> result = chargingSessionRepository.findByChargingPointId(point.getId());

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getChargingPoint().getId()).isEqualTo(point.getId());
  }

  @Test
  @DisplayName("Find active session (endTime is null)")
  void testFindByChargingPointAndEndTimeIsNull() {
    Driver driver = createDriver();
    Car car = createCar();
    ChargingPoint point = createPoint("BMW");

    ChargingSession session = new ChargingSession();
    session.setUser(driver);
    session.setCar(car);
    session.setChargingPoint(point);
    session.setStatus(ChargingStatus.IN_PROGRESS);
    session.setInitialBatteryLevel(30.0);
    session.setStartTime(LocalDateTime.now());
    session.setEndTime(null);
    chargingSessionRepository.save(session);

    Optional<ChargingSession> result =
        chargingSessionRepository.findByChargingPointAndEndTimeIsNull(point);

    assertThat(result).isPresent();
    assertThat(result.get().getEndTime()).isNull();
    assertThat(result.get().getChargingPoint().getId()).isEqualTo(point.getId());
  }

  @Test
  @DisplayName("Return empty if no active session found")
  void testFindByChargingPointAndEndTimeIsNull_None() {
    ChargingPoint point = createPoint("Renault");

    Optional<ChargingSession> result =
        chargingSessionRepository.findByChargingPointAndEndTimeIsNull(point);

    assertThat(result).isEmpty();
  }
}
