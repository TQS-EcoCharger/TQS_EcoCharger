package pt.ua.tqs.ecocharger.ecocharger.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * A ChargingOperator is a User that manages charging stations and their availability
 *
 * @see User
 */
@Entity
@DiscriminatorValue("chargingOperators")
@Table(name = "charging_operator")
@Data
@EqualsAndHashCode(callSuper = true)
public class ChargingOperator extends User {

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
      name = "charging_operator_stations",
      joinColumns = @JoinColumn(name = "operator_id"),
      inverseJoinColumns = @JoinColumn(name = "stations_id"))
    private List<ChargingStation> chargingStations = new ArrayList<ChargingStation>();

    public ChargingOperator(Long id, String email, String password, String name, boolean enabled) {
        super(id, email, password, name, enabled);
    }

    public ChargingOperator() {
        super();
    }

    public void addChargingStation(ChargingStation station) {
        this.chargingStations.add(station);
    }

    public void removeChargingStation(ChargingStation station) {
        this.chargingStations.remove(station);
    }
   
}
