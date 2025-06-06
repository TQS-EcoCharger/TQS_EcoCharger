package pt.ua.tqs.ecocharger.ecocharger.service.interfaces;

import java.util.List;

import pt.ua.tqs.ecocharger.ecocharger.dto.OtpValidationResponse;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingSession;

public interface ChargingSessionService {

  /**
   * Validates an OTP for a charging point at the current time.
   *
   * @param otp the OTP string
   * @param chargingPointId the ID of the charging point
   * @return validation result
   */
  OtpValidationResponse validateOtp(String otp, Long chargingPointId);

  /**
   * Starts a new charging session after OTP validation and reservation check.
   *
   * @param chargingPointId the ID of the charging point
   * @param otp the validated OTP
   * @param carId the ID of the user's car
   * @return the started ChargingSession
   */
  ChargingSession startSessionWithOtp(Long chargingPointId, String otp, Long carId);

  /**
   * Ends an ongoing charging session by calculating duration, energy, cost, and final battery
   * level.
   *
   * @param sessionId the ID of the charging session to end
   * @return the updated ChargingSession
   */
  ChargingSession endSession(Long sessionId);

  /**
   * Retrieves all charging sessions associated with a user ID.
   *
   * @param userId the ID of the user
   * @return a list of ChargingSession objects
   */
  List<ChargingSession> getSessionsByUser(Long userId);

  /**
   * Retrieves all charging sessions in the system.
   *
   * @return a list of all ChargingSession objects
   */
  List<ChargingSession> getAllSessions();
}
