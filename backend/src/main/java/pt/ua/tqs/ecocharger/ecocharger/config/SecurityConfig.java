package pt.ua.tqs.ecocharger.ecocharger.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Autowired private Environment env;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .cors(cors -> cors.configurationSource(corsConfigurationSource()))
      .csrf(csrf -> csrf
          /*
           * The endpoints described in the regex below are not protected by CSRF. This is because they are publicly
           * accessible endpoints. When adding endpoints that do not require CSRF protection, add them to the regex.
           */
          .ignoringRequestMatchers(new RequestMatcher() {
            private final Pattern pattern = Pattern.compile("^/api/auth/.*|^/api/suggestions.*|^/actuator/health/.*");

            @Override
            public boolean matches(HttpServletRequest request) {
              return pattern.matcher(request.getRequestURI()).matches();
            }
          })
      )
      .authorizeHttpRequests(auth -> auth
          .requestMatchers("/api/auth/**").permitAll()
          .requestMatchers("/api/suggestions").permitAll()
          .requestMatchers("/actuator/health/**").permitAll()
          .anyRequest().authenticated());

    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(List.of("http://localhost:5000"));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
    config.setAllowedHeaders(List.of("*"));
    config.setExposedHeaders(List.of("Authorization", "Set-Cookie"));
    config.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }
}
