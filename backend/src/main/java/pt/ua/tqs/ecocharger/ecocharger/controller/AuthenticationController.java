package pt.ua.tqs.ecocharger.ecocharger.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pt.ua.tqs.ecocharger.ecocharger.dto.AuthResultDTO;
import pt.ua.tqs.ecocharger.ecocharger.dto.LoginRequest;
import pt.ua.tqs.ecocharger.ecocharger.models.User;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.AuthenticationService;
import pt.ua.tqs.ecocharger.ecocharger.utils.NotFoundException;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

  private final AuthenticationService authService;

  public AuthenticationController(AuthenticationService authService) {
    this.authService = authService;
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    AuthResultDTO result = authService.authenticate(request.getEmail(), request.getPassword());

    if (!result.isSuccess()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result.getMessage());
    }

    return ResponseEntity.ok(result);
  }

  @PostMapping("/register")
  public ResponseEntity<Object> register(@RequestBody Map<String, String> user) {
    String email = user.get("email");
    String password = user.get("password");
    String name = user.get("name");
    if (email == null || password == null || name == null) {
      AuthResultDTO result = new AuthResultDTO(false, "Missing required fields", null,null);
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

  @GetMapping("/me")
  public ResponseEntity<Object> getCurrentUser(@RequestHeader("Authorization") String token) {
    System.out.println("Received token: " + token);
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
