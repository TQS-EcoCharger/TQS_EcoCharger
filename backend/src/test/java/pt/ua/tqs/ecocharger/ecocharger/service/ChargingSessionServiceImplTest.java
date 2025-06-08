package pt.ua.tqs.ecocharger.ecocharger.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import app.getxray.xray.junit.customjunitxml.annotations.Requirement;
import pt.ua.tqs.ecocharger.ecocharger.dto.ChargingSessionResponseDTO;
import pt.ua.tqs.ecocharger.ecocharger.models.*;
import pt.ua.tqs.ecocharger.ecocharger.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ChargingSessionServiceImplTest {

  @InjectMocks private ChargingSessionServiceImpl service;

  @Mock private OTPCodeRepository otpRepo;
  @Mock private ReservationRepository reservationRepo;
  @Mock private ChargingSessionRepository sessionRepo;
  @Mock private CarRepository carRepo;

  private final Long pointId = 1L;
  private final Long carId = 10L;
  private final String otp = "123456";
  private final LocalDateTime now = LocalDateTime.now();

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testStartSessionWithOtp_success() {
    Reservation reservation = new Reservation();
    reservation.setChargingPoint(new ChargingPoint());
    reservation.setStartTime(now.minusMinutes(5));
    reservation.setEndTime(now.plusMinutes(30));
    reservation.setUser(new User());
    reservation.setStatus(ReservationStatus.TO_BE_USED);

    OTPCode otpCode = new OTPCode();
    otpCode.setCode(otp);
    otpCode.setExpirationTime(now.plusMinutes(10));

    Car car = new Car();
    car.setId(carId);
    car.setBatteryCapacity(40.0);
    car.setBatteryLevel(20.0);

    ChargingSession expectedSession = new ChargingSession();

    when(reservationRepo.findFirstByChargingPointIdAndStartTimeBeforeAndEndTimeAfter(
            eq(pointId), any(), any()))
        .thenReturn(Optional.of(reservation));
    when(otpRepo.findByCodeAndReservation(otp, reservation))
        .thenReturn(Optional.of(otpCode));
    when(carRepo.findById(carId)).thenReturn(Optional.of(car));
    when(sessionRepo.save(any())).thenReturn(expectedSession);

    ChargingSession session = service.startSessionWithOtp(pointId, otp, carId);

    assertNotNull(session);
    assertEquals(ReservationStatus.USED, reservation.getStatus());
    verify(reservationRepo).save(reservation);
    verify(sessionRepo).save(any(ChargingSession.class));
  }

  @Test
  void testEndSession_success() {
    ChargingPoint point = new ChargingPoint();
    point.setChargingRateKWhPerMinute(1.0);
    point.setPricePerKWh(0.5);
    point.setPricePerMinute(0.1);

    Car car = new Car();
    car.setBatteryCapacity(50.0);
    car.setBatteryLevel(20.0);

    ChargingSession session = new ChargingSession();
    session.setId(123L);
    session.setStartTime(now.minusMinutes(30));
    session.setInitialBatteryLevel(10.0);
    session.setChargingPoint(point);
    session.setCar(car);

    when(sessionRepo.findById(123L)).thenReturn(Optional.of(session));
    when(sessionRepo.save(any())).thenReturn(session);

    ChargingSession ended = service.endSession(123L);

    assertNotNull(ended.getEndTime());
    assertEquals(30, ended.getDurationMinutes());
    assertEquals(ChargingStatus.COMPLETED, ended.getStatus());
    assertTrue(ended.getTotalCost() > 0);
    assertEquals(40.0, car.getBatteryLevel()); // 10 + 30 * 1.0 = 40
    verify(carRepo).save(car);
  }

  @Test
  void testEndSession_alreadyEnded_throwsException() {
    ChargingSession session = new ChargingSession();
    session.setEndTime(LocalDateTime.now());

    when(sessionRepo.findById(123L)).thenReturn(Optional.of(session));

    assertThrows(IllegalStateException.class, () -> service.endSession(123L));
  }

  @Test
  void testEndSession_invalidRate_throwsException() {
    ChargingPoint point = new ChargingPoint();
    point.setChargingRateKWhPerMinute(0.00);
    ChargingSession session = new ChargingSession();
    session.setStartTime(now.minusMinutes(10));
    session.setInitialBatteryLevel(5.0);
    session.setChargingPoint(point);
    session.setCar(new Car());

    when(sessionRepo.findById(123L)).thenReturn(Optional.of(session));

    assertThrows(IllegalStateException.class, () -> service.endSession(123L));
  }

  @Test
  void testStartSessionWithOtp_invalidOtp_throwsException() {
    Reservation reservation = new Reservation();
    reservation.setStartTime(now.minusMinutes(5));
    reservation.setEndTime(now.plusMinutes(30));

    when(reservationRepo.findFirstByChargingPointIdAndStartTimeBeforeAndEndTimeAfter(
            anyLong(), any(), any()))
        .thenReturn(Optional.of(reservation));
    when(otpRepo.findByCodeAndReservation(any(), eq(reservation))).thenReturn(Optional.empty());

    assertThrows(
        IllegalArgumentException.class,
        () -> service.startSessionWithOtp(pointId, "invalid", carId));
  }

  @Test
  @Requirement("ET-38")
  void testGetAllSessions_returnsMappedDTOs() {
    ChargingStation station = new ChargingStation();
    station.setId(1L);
    station.setCityName("Porto");
    station.setAddress("Rua A");
    station.setLatitude(41.1579);
    station.setLongitude(-8.6291);

    Connectors connector = new Connectors();
    connector.setId(100L);
    connector.setConnectorType("Type2");
    connector.setVoltageV(400);
    connector.setCurrentA(32);

    ChargingPoint point = new ChargingPoint();
    point.setId(10L);
    point.setBrand("Tesla");
    point.setAvailable(true);
    point.setPricePerKWh(0.30);
    point.setPricePerMinute(0.10);
    point.setChargingRateKWhPerMinute(1.5);
    point.setChargingStation(station);
    point.setConnectors(List.of(connector));

    User user = new User();
    user.setId(200L);
    user.setName("Test User");

    Car car = new Car();
    car.setId(300L);
    car.setName("Nissan Leaf");

    ChargingSession session = new ChargingSession();
    session.setId(500L);
    session.setStartTime(LocalDateTime.now().minusMinutes(45));
    session.setEndTime(LocalDateTime.now());
    session.setTotalCost(5.25);
    session.setStatus(ChargingStatus.COMPLETED);
    session.setInitialBatteryLevel(20.0);
    session.setEnergyDelivered(15.0);
    session.setChargingPoint(point);
    session.setUser(user);
    session.setCar(car);

    when(sessionRepo.findAll()).thenReturn(List.of(session));

    List<ChargingSessionResponseDTO> result = service.getAllSessions();

    assertEquals(1, result.size());
    ChargingSessionResponseDTO dto = result.get(0);
    assertEquals(500L, dto.getId());
    assertEquals(5.25, dto.getTotalCost());
    assertEquals("COMPLETED", dto.getStatus().name());
    assertEquals("Test User", dto.getUser().getName());
    assertEquals("Nissan Leaf", dto.getCar().getName());

    assertNotNull(dto.getChargingPoint());
    assertEquals("Tesla", dto.getChargingPoint().getBrand());
    assertEquals("Porto", dto.getChargingPoint().getChargingStation().getCityName());
    assertEquals(1, dto.getChargingPoint().getConnectors().size());
    assertEquals("Type2", dto.getChargingPoint().getConnectors().get(0).getConnectorType());
  }

  @Test
  @Requirement("ET-561")
  void testGetSessionsByUser_returnsMappedDTOs() {
    Long userId = 200L;

    ChargingStation station = new ChargingStation();
    station.setId(1L);
    station.setCityName("Aveiro");
    station.setAddress("Rua A");
    station.setLatitude(40.0);
    station.setLongitude(-8.0);

    Connectors connector = new Connectors();
    connector.setId(101L);
    connector.setConnectorType("CCS");
    connector.setVoltageV(400);
    connector.setCurrentA(32);

    ChargingPoint point = new ChargingPoint();
    point.setId(10L);
    point.setBrand("Ionity");
    point.setAvailable(true);
    point.setPricePerKWh(0.40);
    point.setPricePerMinute(0.12);
    point.setChargingRateKWhPerMinute(2.0);
    point.setChargingStation(station);
    point.setConnectors(List.of(connector));

    User user = new User();
    user.setId(userId);
    user.setName("Ana Silva");

    Car car = new Car();
    car.setId(301L);
    car.setName("Renault Zoe");

    ChargingSession session = new ChargingSession();
    session.setId(600L);
    session.setStartTime(LocalDateTime.now().minusMinutes(30));
    session.setEndTime(LocalDateTime.now());
    session.setTotalCost(6.00);
    session.setStatus(ChargingStatus.COMPLETED);
    session.setInitialBatteryLevel(30.0);
    session.setEnergyDelivered(12.0);
    session.setChargingPoint(point);
    session.setUser(user);
    session.setCar(car);

    when(sessionRepo.findByUserId(userId)).thenReturn(List.of(session));

    List<ChargingSessionResponseDTO> sessions = service.getSessionsByUser(userId);

    assertEquals(1, sessions.size());
    ChargingSessionResponseDTO dto = sessions.get(0);
    assertEquals(600L, dto.getId());
    assertEquals("Ana Silva", dto.getUser().getName());
    assertEquals("Renault Zoe", dto.getCar().getName());
    assertEquals("Ionity", dto.getChargingPoint().getBrand());
    assertEquals("Aveiro", dto.getChargingPoint().getChargingStation().getCityName());
    assertEquals("CCS", dto.getChargingPoint().getConnectors().get(0).getConnectorType());
  }

  @Test
  @Requirement("ET-42")
  void testEndSession_insufficientBalance_throwsException() {
    ChargingPoint point = new ChargingPoint();
    point.setChargingRateKWhPerMinute(1.0);
    point.setPricePerKWh(1.0);
    point.setPricePerMinute(1.0);

    Driver driver = new Driver();
    driver.setBalance(5.0); // too low to cover cost

    Car car = new Car();
    car.setBatteryCapacity(100.0);
    car.setBatteryLevel(20.0);

    ChargingSession session = new ChargingSession();
    session.setId(700L);
    session.setStartTime(now.minusMinutes(30));
    session.setInitialBatteryLevel(20.0);
    session.setChargingPoint(point);
    session.setUser(driver);
    session.setCar(car);

    when(sessionRepo.findById(700L)).thenReturn(Optional.of(session));

    Exception ex = assertThrows(IllegalStateException.class, () -> service.endSession(700L));
    assertTrue(ex.getMessage().contains("Insufficient balance"));
  }

  @Test
  @Requirement("ET-42")
  void testStartSessionWithOtp_expiredOtp_throwsException() {
    Reservation reservation = new Reservation();
    reservation.setStartTime(now.minusMinutes(5));
    reservation.setEndTime(now.plusMinutes(30));
    reservation.setChargingPoint(new ChargingPoint());
    reservation.setUser(new User());

    OTPCode otpCode = new OTPCode();
    otpCode.setCode(otp);
    otpCode.setExpirationTime(now.minusMinutes(1));
    when(reservationRepo.findFirstByChargingPointIdAndStartTimeBeforeAndEndTimeAfter(
            pointId, now, now))
        .thenReturn(Optional.of(reservation));
    when(otpRepo.findByCodeAndReservation(otp, reservation)).thenReturn(Optional.of(otpCode));

    assertThrows(
        IllegalArgumentException.class, () -> service.startSessionWithOtp(pointId, otp, carId));
  }
}
