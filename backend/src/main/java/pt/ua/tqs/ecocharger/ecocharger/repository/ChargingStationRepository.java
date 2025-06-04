package pt.ua.tqs.ecocharger.ecocharger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import pt.ua.tqs.ecocharger.ecocharger.models.ChargingStation;

@Repository
public interface ChargingStationRepository extends JpaRepository<ChargingStation, Long> {
  Optional<List<ChargingStation>> findByCityName(String cityName);
  List<ChargingStation> findAll();
}
