package pt.ua.tqs.ecocharger.ecocharger.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResultDTO {
  private final boolean success;
  private final String message;
  private final String token;
  private final String userType;
}
