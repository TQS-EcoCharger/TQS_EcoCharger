package pt.ua.tqs.ecocharger.ecocharger.service.interfaces;


import pt.ua.tqs.ecocharger.ecocharger.dto.AuthResultDTO;

public interface AuthenticationService {
  AuthResultDTO authenticate(String email, String password);
  AuthResultDTO register(String email, String password, String name);
}
