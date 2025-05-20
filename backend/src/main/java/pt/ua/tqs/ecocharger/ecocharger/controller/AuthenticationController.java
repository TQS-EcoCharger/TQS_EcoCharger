package pt.ua.tqs.ecocharger.ecocharger.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pt.ua.tqs.ecocharger.ecocharger.dto.AuthResultDTO;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

  private final AuthenticationService authService;

  @Autowired
  public AuthenticationController(AuthenticationService authService) {
    this.authService = authService;
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
    AuthResultDTO result = authService.authenticate(email, password);

    if (!result.isSuccess()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result.getMessage());
    }

    return ResponseEntity.ok(result);
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody Map<String, String> user) {
    String email = user.get("email").strip();
    String password = user.get("password").strip();
    String name = user.get("name").strip();
    if (email == null || password == null || name == null) {
      AuthResultDTO result = new AuthResultDTO(false, "Missing required fields", null);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getMessage());
    }
    AuthResultDTO result = authService.register(email, password, name);

    if (!result.isSuccess()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getMessage());
    }

    return ResponseEntity.ok(result);
  }
}
