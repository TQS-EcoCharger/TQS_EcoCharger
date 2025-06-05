package pt.ua.tqs.ecocharger.ecocharger.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ua.tqs.ecocharger.ecocharger.dto.ReservationRequestDTO;
import pt.ua.tqs.ecocharger.ecocharger.dto.ReservationResponseDTO;
import pt.ua.tqs.ecocharger.ecocharger.models.OTPCode;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.OTPService;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.ReservationService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservation")
public class ReservationController {

  private final ReservationService reservationService;
  private final OTPService otpService;

  public ReservationController(ReservationService reservationService, OTPService otpService) {
    this.otpService = otpService;
    this.reservationService = reservationService;
  }

  @PostMapping
  public ResponseEntity<Object> createReservation(@RequestBody ReservationRequestDTO request) {
    try {
      ReservationResponseDTO response = reservationService.createReservation(request);
      return ResponseEntity.ok(response);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (IllegalStateException e) {
      return ResponseEntity.status(409).body(e.getMessage());
    }
  }

  @GetMapping
  public ResponseEntity<List<ReservationResponseDTO>> getAllReservations() {
    return ResponseEntity.ok(reservationService.getAllReservations());
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<List<ReservationResponseDTO>> getReservationsByUserId(
      @PathVariable Long userId) {
    List<ReservationResponseDTO> reservations = reservationService.getReservationsByUserId(userId);
    return ResponseEntity.ok(reservations);
  }

  @GetMapping("/point/{chargingPointId}/active")
  public ResponseEntity<List<ReservationResponseDTO>> getActiveReservationsByChargingPoint(
      @PathVariable Long chargingPointId) {
    List<ReservationResponseDTO> reservations =
        reservationService.getActiveReservationsByChargingPointId(chargingPointId);
    return ResponseEntity.ok(reservations);
  }

  @PostMapping("/{reservationId}/otp")
  public ResponseEntity<OTPCode> generateOtp(@PathVariable Long reservationId) {
    try {
      OTPCode otp = otpService.generateOtp(reservationId);
      return ResponseEntity.ok(otp);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(null);
    }
  }
}
