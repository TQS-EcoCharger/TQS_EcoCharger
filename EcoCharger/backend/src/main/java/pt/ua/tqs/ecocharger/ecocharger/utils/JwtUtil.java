package pt.ua.tqs.ecocharger.ecocharger.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

  @Value("${jwt.secret}")
  private String jwtSecret;

  private static final long EXPIRATION_TIME = 1000L * 60 * 60 * 24;

  public String generateToken(String email) {
    return JWT.create()
        .withSubject(email)
        .withIssuedAt(new Date())
        .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        .sign(Algorithm.HMAC256(jwtSecret.getBytes()));
  }

  public String getEmailFromToken(String token) {
    return JWT.require(Algorithm.HMAC256(jwtSecret.getBytes())).build().verify(token).getSubject();
  }
}
