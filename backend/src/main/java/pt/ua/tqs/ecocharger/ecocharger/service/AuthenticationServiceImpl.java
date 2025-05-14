package pt.ua.tqs.ecocharger.ecocharger.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import pt.ua.tqs.ecocharger.ecocharger.dto.AuthResultDTO;
import pt.ua.tqs.ecocharger.ecocharger.models.User;
import pt.ua.tqs.ecocharger.ecocharger.repository.UserRepository;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.AuthenticationService;

import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public AuthResultDTO authenticate(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return new AuthResultDTO(false, "User not found");
        }

        User user = userOpt.get();

        if (!user.isEnabled()) {
            return new AuthResultDTO(false, "User is disabled");
        }

        if (!user.getPassword().equals(password)) {
            return new AuthResultDTO(false, "Invalid password");
        }

        return new AuthResultDTO(true, "Login successful");
    }
}
