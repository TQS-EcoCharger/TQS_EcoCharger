package pt.ua.tqs.ecocharger.ecocharger.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

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
@Tag(name = "Reservations", description = "Endpoints for managing reservations")
public class ReservationController {

  private final ReservationService reservationService;
  private final OTPService otpService;

  public ReservationController(ReservationService reservationService, OTPService otpService) {
    this.otpService = otpService;
    this.reservationService = reservationService;
  }

  @Operation(
      summary = "Create a new reservation",
      description = "Creates a reservation for a user on a charging point")
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

  @Operation(
      summary = "Get all reservations",
      description = "Returns all reservations in the system")
  @GetMapping
  public ResponseEntity<List<ReservationResponseDTO>> getAllReservations() {
    return ResponseEntity.ok(reservationService.getAllReservations());
  }

  @Operation(
      summary = "Get reservations by user",
      description = "Returns all reservations associated with a user ID")
  @GetMapping("/user/{userId}")
  public ResponseEntity<List<ReservationResponseDTO>> getReservationsByUserId(
      @Parameter(description = "ID of the user") @PathVariable Long userId) {
    List<ReservationResponseDTO> reservations = reservationService.getReservationsByUserId(userId);
    return ResponseEntity.ok(reservations);
  }

  @Operation(
      summary = "Get active reservations for a charging point",
      description = "Returns active reservations for a specific charging point")
  @GetMapping("/point/{chargingPointId}/active")
  public ResponseEntity<List<ReservationResponseDTO>> getActiveReservationsByChargingPoint(
      @Parameter(description = "ID of the charging point") @PathVariable Long chargingPointId) {
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
