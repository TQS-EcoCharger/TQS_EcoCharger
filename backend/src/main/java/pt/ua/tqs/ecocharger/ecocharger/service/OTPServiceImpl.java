package pt.ua.tqs.ecocharger.ecocharger.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import pt.ua.tqs.ecocharger.ecocharger.models.OTPCode;
import pt.ua.tqs.ecocharger.ecocharger.models.Reservation;
import pt.ua.tqs.ecocharger.ecocharger.models.ReservationStatus;
import pt.ua.tqs.ecocharger.ecocharger.repository.OTPCodeRepository;
import pt.ua.tqs.ecocharger.ecocharger.repository.ReservationRepository;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.OTPService;

@Service
public class OTPServiceImpl implements OTPService {

  private final OTPCodeRepository otpCodeRepository;
  private final ReservationRepository reservationRepository;

  public OTPServiceImpl(
      OTPCodeRepository otpCodeRepository, ReservationRepository reservationRepository) {
    this.reservationRepository = reservationRepository;
    this.otpCodeRepository = otpCodeRepository;
  }

  public OTPCode generateOtp(Long reservationId) {
    Reservation reservation =
        reservationRepository
            .findById(reservationId)
            .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

    if (reservation.getStatus() != ReservationStatus.TO_BE_USED) {
      throw new IllegalStateException("OTP can only be generated for TO_BE_USED reservations");
    }

    otpCodeRepository
        .findByReservation(reservation)
        .ifPresent(existingOtp -> otpCodeRepository.delete(existingOtp));

    String code = String.format("%06d", new SecureRandom().nextInt(1000000));
    LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(5);

    OTPCode newOtp = new OTPCode(null, code, expirationTime, reservation);
    return otpCodeRepository.save(newOtp);
  }

  public boolean validateOtp(String code, Reservation reservation) {
    Optional<OTPCode> otp = otpCodeRepository.findByCodeAndReservation(code, reservation);
    return otp.isPresent() && otp.get().getExpirationTime().isAfter(LocalDateTime.now());
  }
}
