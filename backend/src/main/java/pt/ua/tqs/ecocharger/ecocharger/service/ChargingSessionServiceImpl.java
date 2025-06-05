package pt.ua.tqs.ecocharger.ecocharger.service;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import pt.ua.tqs.ecocharger.ecocharger.dto.OtpValidationResponse;
import pt.ua.tqs.ecocharger.ecocharger.models.Car;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingPoint;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingSession;
import pt.ua.tqs.ecocharger.ecocharger.models.Reservation;
import pt.ua.tqs.ecocharger.ecocharger.models.ReservationStatus;
import pt.ua.tqs.ecocharger.ecocharger.repository.CarRepository;
import pt.ua.tqs.ecocharger.ecocharger.repository.ChargingSessionRepository;
import pt.ua.tqs.ecocharger.ecocharger.repository.OTPCodeRepository;
import pt.ua.tqs.ecocharger.ecocharger.repository.ReservationRepository;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.ChargingSessionService;
import pt.ua.tqs.ecocharger.ecocharger.models.OTPCode;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingStatus;
import java.util.Optional;

@Service
public class ChargingSessionServiceImpl implements ChargingSessionService {

    private final OTPCodeRepository otpCodeRepository;
    private final ReservationRepository reservationRepository;
    private final ChargingSessionRepository chargingSessionRepository;
    private final CarRepository carRepository;

    public ChargingSessionServiceImpl(OTPCodeRepository otpCodeRepository,
                                      ReservationRepository reservationRepository,
                                      ChargingSessionRepository chargingSessionRepository,
                                      CarRepository carRepository) {
        this.otpCodeRepository = otpCodeRepository;
        this.reservationRepository = reservationRepository;
        this.chargingSessionRepository = chargingSessionRepository;
        this.carRepository = carRepository;
    }

    @Override
    public OtpValidationResponse validateOtp(String otp, Long chargingPointId) {
        LocalDateTime now = LocalDateTime.now();

        System.out.println(now);
        Optional<Reservation> reservation = reservationRepository
            .findFirstByChargingPointIdAndStartTimeBeforeAndEndTimeAfter(chargingPointId, now, now);

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

        Reservation reservation = reservationRepository
                .findFirstByChargingPointIdAndStartTimeBeforeAndEndTimeAfter(chargingPointId, now, now)
                .orElseThrow(() -> new IllegalArgumentException("No active reservation found for this charging point."));

        OTPCode code = otpCodeRepository.findByCodeAndReservation(otp, reservation)
                .orElseThrow(() -> new IllegalArgumentException("Invalid OTP."));

        if (code.getExpirationTime().isBefore(now)) {
            throw new IllegalArgumentException("OTP expired.");
        }

        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new IllegalArgumentException("Car not found."));

        // ✅ Mark reservation as USED
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
        ChargingSession session = chargingSessionRepository.findById(sessionId)
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
            throw new IllegalStateException("Invalid charging rate for charging point.");
        }

        double chargedEnergy = durationMinutes * chargingRateKWhPerMinute;
        double newBatteryLevel = Math.min(initialBattery + chargedEnergy, capacity);
        double actualEnergyDelivered = newBatteryLevel - initialBattery;

        double costPerKWh = chargingPoint.getPricePerKWh() != null ? chargingPoint.getPricePerKWh() : 0.0;
        double costPerMinute = chargingPoint.getPricePerMinute() != null ? chargingPoint.getPricePerMinute() : 0.0;

        double totalCost = (actualEnergyDelivered * costPerKWh) + (durationMinutes * costPerMinute);

        session.setTotalCost(totalCost);
        session.setStatus(ChargingStatus.COMPLETED);

        car.setBatteryLevel(newBatteryLevel);
        carRepository.save(car);

        return chargingSessionRepository.save(session);
    }



}