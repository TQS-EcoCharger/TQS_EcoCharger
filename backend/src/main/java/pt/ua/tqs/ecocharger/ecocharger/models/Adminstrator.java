package pt.ua.tqs.ecocharger.ecocharger.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "administrators")
public class Adminstrator extends User {

    public Adminstrator() {
        super();
    }

    public Adminstrator(Long id, String email, String password, String name, boolean enabled) {
        super(id, email, password, name, enabled);
    }

    @OneToMany(mappedBy = "addedBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChargingStation> addedStations = new ArrayList<>();


    @Override
    public String toString() {
        return "Adminstrator" +
                "id=" + getId() +
                ", email='" + getEmail() + '\'' +
                ", name='" + getName() + '\'' +
                ", enabled=" + isEnabled();
    }
    
}
