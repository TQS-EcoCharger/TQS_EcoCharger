package pt.ua.tqs.ecocharger.ecocharger.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ua.tqs.ecocharger.ecocharger.dto.AuthResultDTO;
import pt.ua.tqs.ecocharger.ecocharger.models.User;
import pt.ua.tqs.ecocharger.ecocharger.repository.UserRepository;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.AuthenticationService;
import pt.ua.tqs.ecocharger.ecocharger.utils.JwtUtil;

import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

  @Autowired private UserRepository userRepository;

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

    String token = JwtUtil.generateToken(user.getEmail());

    return new AuthResultDTO(true, "Login successful", token);
  }
}
