package pt.ua.tqs.ecocharger.ecocharger.service;

import org.springframework.stereotype.Service;
import pt.ua.tqs.ecocharger.ecocharger.dto.AuthResultDTO;
import pt.ua.tqs.ecocharger.ecocharger.models.Administrator;
import pt.ua.tqs.ecocharger.ecocharger.models.User;
import pt.ua.tqs.ecocharger.ecocharger.repository.UserRepository;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.AuthenticationService;
import pt.ua.tqs.ecocharger.ecocharger.utils.JwtUtil;

import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

  private final UserRepository userRepository;
  private final JwtUtil jwtUtil;

  public AuthenticationServiceImpl(UserRepository userRepository, JwtUtil jwtUtil) {
    this.userRepository = userRepository;
    this.jwtUtil = jwtUtil;
  }

  @Override
  public AuthResultDTO authenticate(String email, String password) {
    Optional<User> userOpt = userRepository.findByEmail(email);

    if (userOpt.isEmpty()) {
      return new AuthResultDTO(false, "User not found", null, null);
    }

    User user = userOpt.get();

    if (!user.isEnabled()) {
      return new AuthResultDTO(false, "User is disabled", null, null);
    }

    if (!user.getPassword().equals(password)) {
      return new AuthResultDTO(false, "Invalid password", null, null);
    }


    String token = jwtUtil.generateToken(user.getEmail());
    String userType = user instanceof Administrator ? "administrator" : "client";

    return new AuthResultDTO(true, "Login successful", token, userType);
  }

  @Override
  public AuthResultDTO register(String email, String password, String name) {
    if (userRepository.findByEmail(email).isPresent()) {
      return new AuthResultDTO(false, "Email already in use", null, null);
    }

    if (password.length() < 6) {
      return new AuthResultDTO(false, "Password must be at least 6 characters", null, null);
    }

    if (name.length() < 3) {
      return new AuthResultDTO(false, "Name must be at least 3 characters", null, null);
    }

    if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
      return new AuthResultDTO(false, "Invalid email format", null, null);
    }

    User user = new User();
    user.setEmail(email);
    user.setName(name);
    user.setPassword(password); 
    user.setEnabled(true);

    userRepository.save(user);

    String token = jwtUtil.generateToken(user.getEmail());
    return new AuthResultDTO(true, "Registration successful", token, "client");
  }
}
