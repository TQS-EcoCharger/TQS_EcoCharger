package pt.ua.tqs.ecocharger.ecocharger.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.ua.tqs.ecocharger.ecocharger.models.Driver;

public interface DriverRepository extends JpaRepository<Driver, Long> {
  Optional<Driver> findByEmail(String email);
}
