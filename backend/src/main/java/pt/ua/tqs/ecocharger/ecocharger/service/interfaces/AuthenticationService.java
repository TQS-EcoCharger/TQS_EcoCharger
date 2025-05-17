package pt.ua.tqs.ecocharger.ecocharger.service.interfaces;

import org.springframework.http.ResponseEntity;

import pt.ua.tqs.ecocharger.ecocharger.dto.AuthResultDTO;

public interface AuthenticationService {
    AuthResultDTO authenticate(String email, String password);

}
