package pt.ua.tqs.ecocharger.ecocharger.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A Driver is a User that register cars and uses the app to find, book and use charging stations
 *
 * @see User
 */
@Entity
@DiscriminatorValue("drivers")
@Table(name = "drivers")
@Data
@NoArgsConstructor
public class Driver extends User {

  @Column(name = "balance", nullable = false)
  private Double balance = 0.0;

  @OneToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "driver_cars",
      joinColumns = @JoinColumn(name = "driver_id"),
      inverseJoinColumns = @JoinColumn(name = "cars_id"))
  private List<Car> cars = new ArrayList<>();

  public Driver(Long id, String email, String password, String name, boolean enabled) {
    super(id, email, password, name, enabled);
    this.cars = new ArrayList<>();
  }

  public void addCar(Car car) {
    this.cars.add(car);
  }

  public void removeCar(Car car) {
    this.cars.remove(car);
  }
}
