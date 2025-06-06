package pt.ua.tqs.ecocharger.ecocharger.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import pt.ua.tqs.ecocharger.ecocharger.dto.AuthResultDTO;
import pt.ua.tqs.ecocharger.ecocharger.dto.LoginRequest;
import pt.ua.tqs.ecocharger.ecocharger.models.User;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.AuthenticationService;
import pt.ua.tqs.ecocharger.ecocharger.utils.NotFoundException;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for user login, registration, and current user info")
public class AuthenticationController {

  private final AuthenticationService authService;

  public AuthenticationController(AuthenticationService authService) {
    this.authService = authService;
  }

  @Operation(
      summary = "User login",
      description = "Authenticates a user with email and password, returns a token if successful")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully authenticated and returned token",
      content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = AuthResultDTO.class)))
  @ApiResponse(responseCode = "401", description = "Invalid credentials or authentication failed")
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    AuthResultDTO result = authService.authenticate(request.getEmail(), request.getPassword());

    if (!result.isSuccess()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result.getMessage());
    }

    return ResponseEntity.ok(result);
  }

  @Operation(
      summary = "User registration",
      description = "Registers a new user with email, password, and name")
  @ApiResponse(
      responseCode = "200",
      description = "User registered successfully",
      content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = AuthResultDTO.class)))
  @ApiResponse(responseCode = "400", description = "Missing required fields or invalid input")
  @PostMapping("/register")
  public ResponseEntity<Object> register(@RequestBody Map<String, String> user) {
    String email = user.get("email");
    String password = user.get("password");
    String name = user.get("name");

    if (email == null || password == null || name == null) {
      AuthResultDTO result = new AuthResultDTO(false, "Missing required fields", null, null);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getMessage());
    }

    email = email.strip();
    password = password.strip();
    name = name.strip();

    AuthResultDTO result = authService.register(email, password, name);

    if (!result.isSuccess()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getMessage());
    }

    return ResponseEntity.ok(result);
  }

  @Operation(
      summary = "Get current user",
      description = "Retrieves the current authenticated user based on JWT token")
  @ApiResponse(
      responseCode = "200",
      description = "Current authenticated user retrieved successfully",
      content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = User.class)))
  @ApiResponse(responseCode = "401", description = "Invalid or missing authentication token")
  @ApiResponse(responseCode = "403", description = "User not authorized or not found")
  @GetMapping("/me")
  public ResponseEntity<Object> getCurrentUser(
      @Parameter(description = "Bearer token obtained at login") @RequestHeader("Authorization") String token) {
    String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;

    try {
      User currentUser = authService.getCurrentUser(jwtToken);
      return ResponseEntity.ok(currentUser);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    } catch (NotFoundException e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }
  }
}
