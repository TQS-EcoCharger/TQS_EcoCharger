package pt.ua.tqs.ecocharger.ecocharger.service.interfaces;

import pt.ua.tqs.ecocharger.ecocharger.dto.OtpValidationResponse;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingSession;


public interface ChargingSessionService {
    ChargingSession startSessionWithOtp(Long chargingPointId, String otp, Long carId);
    OtpValidationResponse validateOtp(String otp, Long chargingPointId);

}