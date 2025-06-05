package pt.ua.tqs.ecocharger.ecocharger.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pt.ua.tqs.ecocharger.ecocharger.dto.OtpValidationRequestDTO;
import pt.ua.tqs.ecocharger.ecocharger.dto.OtpValidationResponse;
import pt.ua.tqs.ecocharger.ecocharger.dto.StartChargingRequestDTO;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingSession;
import pt.ua.tqs.ecocharger.ecocharger.models.OTPCode;
import pt.ua.tqs.ecocharger.ecocharger.models.Reservation;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.ChargingSessionService;

@RestController
@RequestMapping("/api/v1/sessions")
public class ChargingSessionController {

    private final ChargingSessionService chargingSessionService;

    public ChargingSessionController(ChargingSessionService chargingSessionService) {
        this.chargingSessionService = chargingSessionService;
    }


    @PostMapping("/validate-otp")
    public ResponseEntity<OtpValidationResponse> validateOtp(@RequestBody OtpValidationRequestDTO request) {
        OtpValidationResponse response = chargingSessionService.validateOtp(request.getOtp(), request.getChargingPointId());
        
        if (!response.isValid()) {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Starts a new charging session after validating OTP and car.
     * If a reservation is present, it is used. Otherwise, session proceeds without it.
     */
    @PostMapping
    public ResponseEntity<?> startCharging(@RequestBody StartChargingRequestDTO request) {
        try {
            ChargingSession session = chargingSessionService.startSessionWithOtp(
                request.getChargingPointId(),
                request.getOtp(),
                request.getCarId()
            );

            return ResponseEntity.ok(session);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/{sessionId}/end")
    public ResponseEntity<?> endCharging(@PathVariable Long sessionId) {
        try {
            ChargingSession endedSession = chargingSessionService.endSession(sessionId);
            return ResponseEntity.ok(endedSession);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
