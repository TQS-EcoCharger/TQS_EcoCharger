package pt.ua.tqs.ecocharger.ecocharger.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * A Driver is a User that register cars and uses the app to find, book and use
 * charging stations
 *
 * @see User
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "driver")
public class Driver extends User {

    @OneToMany(mappedBy = "driver", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Car> cars = new ArrayList<>();

    public Driver(Long id, String email, String password, String name, boolean enabled) {
        super(id, email, password, name, enabled);
        this.cars = new ArrayList<>();
    }

    public Driver() {
        super();
        this.cars = new ArrayList<>();
    }

    public void addCar(Car car) {
        this.cars.add(car);
    }

    public void removeCar(Car car) {
        this.cars.remove(car);
    }
}
