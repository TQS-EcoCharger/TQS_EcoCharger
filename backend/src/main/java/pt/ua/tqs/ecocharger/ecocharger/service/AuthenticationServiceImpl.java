package pt.ua.tqs.ecocharger.ecocharger.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.ua.tqs.ecocharger.ecocharger.dto.AuthResultDTO;
import pt.ua.tqs.ecocharger.ecocharger.models.User;
import pt.ua.tqs.ecocharger.ecocharger.repository.UserRepository;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.AuthenticationService;
import pt.ua.tqs.ecocharger.ecocharger.utils.JwtUtil;
import pt.ua.tqs.ecocharger.ecocharger.utils.NotFoundException;

import java.util.Optional;

import com.auth0.jwt.exceptions.SignatureVerificationException;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

  @Autowired private UserRepository userRepository;

  @Autowired private JwtUtil jwtUtil;

  @Override
  public AuthResultDTO authenticate(String email, String password) {
    Optional<User> userOpt = userRepository.findByEmail(email);

    if (userOpt.isEmpty()) {
      return new AuthResultDTO(false, "User not found", null);
    }

    User user = userOpt.get();

    if (!user.isEnabled()) {
      return new AuthResultDTO(false, "User is disabled", null);
    }

    if (!user.getPassword().equals(password)) {
      return new AuthResultDTO(false, "Invalid password", null);
    }

    String token = jwtUtil.generateToken(user.getEmail());

    return new AuthResultDTO(true, "Login successful", token);
  }

  @Override
  public AuthResultDTO register(String email, String password, String name) {
    Optional<User> existingUserOpt = userRepository.findByEmail(email);
    if (existingUserOpt.isPresent()) {
      return new AuthResultDTO(false, "Email already in use", null);
    }

    if (password.length() < 6) {
      return new AuthResultDTO(false, "Password must be at least 6 characters", null);
    }

    if (name.length() < 3) {
      return new AuthResultDTO(false, "Name must be at least 3 characters", null);
    }

    if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
      return new AuthResultDTO(false, "Invalid email format", null);
    }

    User user = new User();
    user.setEmail(email);
    user.setPassword(password);
    user.setName(name);
    user.setEnabled(true);

    userRepository.save(user);

    String token = jwtUtil.generateToken(user.getEmail());

    return new AuthResultDTO(true, "Registration successful", token);
  }

  @Override
  public User getCurrentUser(String token) {
    try {
      String email = jwtUtil.getEmailFromToken(token);

      if (email == null) {
        throw new IllegalArgumentException("Invalid token");
      }

      Optional<User> userOpt = userRepository.findByEmail(email);
      if (userOpt.isEmpty()) {
        throw new NotFoundException("User not found");
      }

      User user = userOpt.get();
      if (!user.isEnabled()) {
        throw new IllegalArgumentException("User is disabled");
      }

      return user;
    } catch (SignatureVerificationException e) {
      throw new IllegalArgumentException("Invalid token signature (the police is on the way)");
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Invalid token format");
    }
  }
}
