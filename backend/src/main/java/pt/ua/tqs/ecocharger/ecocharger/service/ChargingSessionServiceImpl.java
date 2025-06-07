package pt.ua.tqs.ecocharger.ecocharger.service;

import org.springframework.stereotype.Service;

import pt.ua.tqs.ecocharger.ecocharger.dto.ChargingPointDTO;
import pt.ua.tqs.ecocharger.ecocharger.dto.ChargingSessionResponseDTO;
import pt.ua.tqs.ecocharger.ecocharger.dto.ChargingStationDTO;
import pt.ua.tqs.ecocharger.ecocharger.dto.ConnectorDTO;
import pt.ua.tqs.ecocharger.ecocharger.dto.OtpValidationResponse;
import pt.ua.tqs.ecocharger.ecocharger.models.*;
import pt.ua.tqs.ecocharger.ecocharger.repository.*;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.ChargingSessionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ChargingSessionServiceImpl implements ChargingSessionService {

  private final OTPCodeRepository otpCodeRepository;
  private final ReservationRepository reservationRepository;
  private final ChargingSessionRepository chargingSessionRepository;
  private final CarRepository carRepository;
  private final UserRepository userRepository;

  public ChargingSessionServiceImpl(
      OTPCodeRepository otpCodeRepository,
      ReservationRepository reservationRepository,
      ChargingSessionRepository chargingSessionRepository,
      CarRepository carRepository,
      UserRepository userRepository) {
    this.otpCodeRepository = otpCodeRepository;
    this.reservationRepository = reservationRepository;
    this.chargingSessionRepository = chargingSessionRepository;
    this.carRepository = carRepository;
    this.userRepository = userRepository;
  }

  @Override
  public OtpValidationResponse validateOtp(String otp, Long chargingPointId) {
    LocalDateTime now = LocalDateTime.now();

    Optional<Reservation> reservation =
        reservationRepository.findFirstByChargingPointIdAndStartTimeBeforeAndEndTimeAfter(
            chargingPointId, now, now);

    if (reservation.isEmpty()) {
      return new OtpValidationResponse(false, "No active reservation found.");
    }

    Optional<OTPCode> code = otpCodeRepository.findByCodeAndReservation(otp, reservation.get());

    if (code.isEmpty()) {
      return new OtpValidationResponse(false, "Invalid OTP.");
    }

    if (code.get().getExpirationTime().isBefore(now)) {
      return new OtpValidationResponse(false, "OTP has expired.");
    }

    return new OtpValidationResponse(true, "OTP is valid.");
  }

  @Override
  public ChargingSession startSessionWithOtp(Long chargingPointId, String otp, Long carId) {
    LocalDateTime now = LocalDateTime.now();

    Reservation reservation =
        reservationRepository
            .findFirstByChargingPointIdAndStartTimeBeforeAndEndTimeAfter(chargingPointId, now, now)
            .orElseThrow(() -> new IllegalArgumentException("No active reservation found."));

    OTPCode code =
        otpCodeRepository
            .findByCodeAndReservation(otp, reservation)
            .orElseThrow(() -> new IllegalArgumentException("Invalid OTP."));

    if (code.getExpirationTime().isBefore(now)) {
      throw new IllegalArgumentException("OTP expired.");
    }

    Car car =
        carRepository
            .findById(carId)
            .orElseThrow(() -> new IllegalArgumentException("Car not found."));

    reservation.setStatus(ReservationStatus.USED);
    reservationRepository.save(reservation);

    ChargingSession session = new ChargingSession();
    session.setReservation(reservation);
    session.setUser(reservation.getUser());
    session.setChargingPoint(reservation.getChargingPoint());
    session.setCar(car);
    session.setStartTime(now);
    session.setInitialBatteryLevel(car.getBatteryLevel());
    session.setStatus(ChargingStatus.IN_PROGRESS);

    return chargingSessionRepository.save(session);
  }

  @Override
  public ChargingSession endSession(Long sessionId) {
    ChargingSession session =
        chargingSessionRepository
            .findById(sessionId)
            .orElseThrow(() -> new IllegalArgumentException("Session not found."));

    if (session.getEndTime() != null) {
      throw new IllegalStateException("Session already ended.");
    }

    LocalDateTime endTime = LocalDateTime.now();
    session.setEndTime(endTime);

    long durationMinutes = java.time.Duration.between(session.getStartTime(), endTime).toMinutes();
    session.setDurationMinutes(durationMinutes);

    Car car = session.getCar();
    ChargingPoint chargingPoint = session.getChargingPoint();

    double initialBattery = session.getInitialBatteryLevel();
    double capacity = car.getBatteryCapacity();

    double chargingRateKWhPerMinute = chargingPoint.getChargingRateKWhPerMinute();
    if (chargingRateKWhPerMinute <= 0) {
      throw new IllegalStateException("Invalid charging rate.");
    }

    double chargedEnergy = durationMinutes * chargingRateKWhPerMinute;
    double newBatteryLevel = Math.min(initialBattery + chargedEnergy, capacity);
    double actualEnergyDelivered = newBatteryLevel - initialBattery;

    double costPerKWh = Optional.ofNullable(chargingPoint.getPricePerKWh()).orElse(0.0);
    double costPerMinute = Optional.ofNullable(chargingPoint.getPricePerMinute()).orElse(0.0);

    double totalCost = (actualEnergyDelivered * costPerKWh) + (durationMinutes * costPerMinute);
    session.setEnergyDelivered(actualEnergyDelivered);
    session.setTotalCost(totalCost);
    session.setStatus(ChargingStatus.COMPLETED);

    car.setBatteryLevel(newBatteryLevel);
    carRepository.save(car);

    if (session.getUser() instanceof Driver driver) {
      double newBalance = driver.getBalance() - totalCost;
      if (newBalance < 0) {
        throw new IllegalStateException(
            "Insufficient balance to complete the session, please recharge your balance.");
      }
      driver.setBalance(newBalance);
      userRepository.save(driver);
    }

    return chargingSessionRepository.save(session);
  }

  public List<ChargingSessionResponseDTO> getAllSessions() {
    return chargingSessionRepository.findAll().stream()
        .map(
            session -> {
              ChargingPoint point = session.getChargingPoint();
              ChargingStation station = point.getChargingStation();

              List<ConnectorDTO> connectorDtos =
                  point.getConnectors().stream()
                      .map(
                          conn ->
                              new ConnectorDTO(
                                  conn.getId(),
                                  conn.getConnectorType(),
                                  conn.getRatedPowerKW(),
                                  conn.getVoltageV(),
                                  conn.getCurrentA()))
                      .toList();

              ChargingStationDTO stationDto =
                  new ChargingStationDTO(
                      station.getId(),
                      station.getCityName(),
                      station.getAddress(),
                      station.getLatitude(),
                      station.getLongitude());

              ChargingPointDTO pointDto =
                  new ChargingPointDTO(
                      point.getId(),
                      point.getBrand(),
                      point.isAvailable(),
                      point.getPricePerKWh(),
                      point.getPricePerMinute(),
                      point.getChargingRateKWhPerMinute(),
                      connectorDtos,
                      stationDto);

              return new ChargingSessionResponseDTO(
                  session.getId(),
                  session.getStartTime(),
                  session.getEndTime(),
                  session.getDurationMinutes(),
                  session.getTotalCost(),
                  session.getStatus(),
                  session.getInitialBatteryLevel(),
                  session.getEnergyDelivered(),
                  pointDto,
                  session.getUser(),
                  session.getCar());
            })
        .toList();
  }

  @Override
  public List<ChargingSessionResponseDTO> getSessionsByUser(Long userId) {
    List<ChargingSession> sessions = chargingSessionRepository.findByUserId(userId);

    return sessions.stream()
        .map(
            session -> {
              ChargingStation station = session.getChargingPoint().getChargingStation();

              ChargingStationDTO stationDTO =
                  new ChargingStationDTO(
                      station.getId(),
                      station.getCityName(),
                      station.getAddress(),
                      station.getLatitude(),
                      station.getLongitude());

              List<ConnectorDTO> connectors =
                  session.getChargingPoint().getConnectors().stream()
                      .map(
                          connector ->
                              new ConnectorDTO(
                                  connector.getId(),
                                  connector.getConnectorType(),
                                  connector.getRatedPowerKW(),
                                  connector.getVoltageV(),
                                  connector.getCurrentA()))
                      .toList();

              ChargingPointDTO pointDTO =
                  new ChargingPointDTO(
                      session.getChargingPoint().getId(),
                      session.getChargingPoint().getBrand(),
                      session.getChargingPoint().isAvailable(),
                      session.getChargingPoint().getPricePerKWh(),
                      session.getChargingPoint().getPricePerMinute(),
                      session.getChargingPoint().getChargingRateKWhPerMinute(),
                      connectors,
                      stationDTO);

              return new ChargingSessionResponseDTO(
                  session.getId(),
                  session.getStartTime(),
                  session.getEndTime(),
                  session.getDurationMinutes(),
                  session.getTotalCost(),
                  session.getStatus(),
                  session.getInitialBatteryLevel(),
                  session.getEnergyDelivered(),
                  pointDTO,
                  session.getUser(),
                  session.getCar());
            })
        .toList();
  }
}
