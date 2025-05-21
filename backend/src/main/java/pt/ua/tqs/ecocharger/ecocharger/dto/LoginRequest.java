package pt.ua.tqs.ecocharger.ecocharger.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}