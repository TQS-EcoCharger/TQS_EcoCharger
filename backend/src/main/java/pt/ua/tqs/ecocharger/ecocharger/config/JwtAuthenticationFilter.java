package pt.ua.tqs.ecocharger.ecocharger.config;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import pt.ua.tqs.ecocharger.ecocharger.models.User;
import pt.ua.tqs.ecocharger.ecocharger.repository.UserRepository;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Lazy @Autowired private UserRepository userRepository;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
      String path = request.getRequestURI();

      // Skip actuator endpoints
      if (path.startsWith("/actuator")) {
          filterChain.doFilter(request, response);
          return;
      }
      String token = extractTokenFromHeader(request);

    if (token != null) {
      try {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());
        DecodedJWT decodedJWT = JWT.require(algorithm).build().verify(token);

        String email = decodedJWT.getClaim("sub").asString();

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
          User user = userOpt.get();

          UsernamePasswordAuthenticationToken authentication =
              new UsernamePasswordAuthenticationToken(user, null, List.of());

          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      } catch (Exception e) {
        System.out.println("Token inválido: " + e.getMessage());
      }
    }

    filterChain.doFilter(request, response);
  }

  private String extractTokenFromHeader(HttpServletRequest request) {
    String header = request.getHeader("Authorization");
    if (header != null && header.startsWith("Bearer ")) {
      return header.substring(7);
    }
    return null;
  }
}
