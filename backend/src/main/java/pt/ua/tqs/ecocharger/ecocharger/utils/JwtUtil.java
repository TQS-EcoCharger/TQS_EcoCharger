package pt.ua.tqs.ecocharger.ecocharger.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Date;
import javax.crypto.SecretKey;

public class JwtUtil {

  private static final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
  private static final long EXPIRATION_TIME = 86400000;

  public static String generateToken(String subject) {
    return Jwts.builder()
        .setSubject(subject)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        .signWith(key)
        .compact();
  }
}
