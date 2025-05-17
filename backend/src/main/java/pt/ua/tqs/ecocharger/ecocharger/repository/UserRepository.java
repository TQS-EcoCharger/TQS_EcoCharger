package pt.ua.tqs.ecocharger.ecocharger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.ua.tqs.ecocharger.ecocharger.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);
}
