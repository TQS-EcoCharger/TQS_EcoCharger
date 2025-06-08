package pt.ua.tqs.ecocharger.ecocharger.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import pt.ua.tqs.ecocharger.ecocharger.dto.OtpValidationRequestDTO;
import pt.ua.tqs.ecocharger.ecocharger.dto.OtpValidationResponse;
import pt.ua.tqs.ecocharger.ecocharger.dto.StartChargingRequestDTO;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingSession;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.ChargingSessionService;

@RestController
@RequestMapping("/api/v1/sessions")
@Tag(name = "Charging Sessions", description = "Endpoints for managing charging sessions")
public class ChargingSessionController {

  private final ChargingSessionService chargingSessionService;

  public ChargingSessionController(ChargingSessionService chargingSessionService) {
    this.chargingSessionService = chargingSessionService;
  }

  @Operation(
      summary = "Validate OTP for charging point",
      description = "Validates the OTP provided for a charging point before starting a session")
  @ApiResponse(
      responseCode = "200",
      description = "OTP validation result",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = OtpValidationResponse.class)))
  @ApiResponse(responseCode = "400", description = "Invalid OTP")
  @PostMapping("/validate-otp")
  public ResponseEntity<OtpValidationResponse> validateOtp(
      @RequestBody OtpValidationRequestDTO request) {
    OtpValidationResponse response =
        chargingSessionService.validateOtp(request.getOtp(), request.getChargingPointId());

    if (!response.isValid()) {
      return ResponseEntity.badRequest().body(response);
    }

    return ResponseEntity.ok(response);
  }

  /**
   * Starts a new charging session after validating OTP and car. If a reservation is present, it is
   * used. Otherwise, session proceeds without it.
   */
  @Operation(
      summary = "Start a charging session",
      description = "Starts a charging session given a charging point ID, OTP, and car ID")
  @ApiResponse(
      responseCode = "200",
      description = "Charging session started successfully",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ChargingSession.class)))
  @ApiResponse(responseCode = "400", description = "Invalid input or OTP")
  @ApiResponse(
      responseCode = "409",
      description = "Session conflict or other business rules violation")
  @PostMapping
  public ResponseEntity<Object> startCharging(@RequestBody StartChargingRequestDTO request) {
    try {
      ChargingSession session =
          chargingSessionService.startSessionWithOtp(
              request.getChargingPointId(), request.getOtp(), request.getCarId());

      return ResponseEntity.ok(session);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    } catch (IllegalStateException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
  }

  @Operation(
      summary = "End a charging session",
      description = "Ends an active charging session by its session ID")
  @ApiResponse(
      responseCode = "200",
      description = "Charging session ended successfully",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ChargingSession.class)))
  @ApiResponse(responseCode = "404", description = "Session not found")
  @ApiResponse(
      responseCode = "409",
      description = "Session cannot be ended due to conflict or invalid state")
  @PostMapping("/{sessionId}/end")
  public ResponseEntity<Object> endCharging(
      @Parameter(description = "ID of the charging session to end") @PathVariable Long sessionId) {
    try {
      ChargingSession endedSession = chargingSessionService.endSession(sessionId);
      return ResponseEntity.ok(endedSession);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (IllegalStateException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
  }

  @Operation(
      summary = "Get all charging sessions for a user",
      description = "Returns a list of all charging sessions associated with a specific user ID")
  @ApiResponse(
      responseCode = "200",
      description = "List of sessions returned successfully",
      content = @Content(mediaType = "application/json"))
  @GetMapping("/user/{userId}")
  public ResponseEntity<Object> getChargingSessionsByUser(
      @Parameter(description = "ID of the user") @PathVariable Long userId) {
    try {
      var sessions = chargingSessionService.getSessionsByUser(userId);
      return ResponseEntity.ok(sessions);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error: " + e.getMessage());
    }
  }

  @Operation(
      summary = "Get all charging sessions",
      description = "Returns a list of all charging sessions from all users")
  @ApiResponse(
      responseCode = "200",
      description = "List of all charging sessions returned successfully",
      content = @Content(mediaType = "application/json"))
  @GetMapping
  public ResponseEntity<Object> getAllChargingSessions() {
    try {
      var sessions = chargingSessionService.getAllSessions();
      return ResponseEntity.ok(sessions);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error: " + e.getMessage());
    }
  }
}
