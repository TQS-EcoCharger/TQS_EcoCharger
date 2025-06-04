package pt.ua.tqs.ecocharger.ecocharger.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.ua.tqs.ecocharger.ecocharger.models.Car;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
  List<Car> findByLicensePlate(String licensePlate);

  List<Car> findByMakeAndModel(String make, String model);
}
