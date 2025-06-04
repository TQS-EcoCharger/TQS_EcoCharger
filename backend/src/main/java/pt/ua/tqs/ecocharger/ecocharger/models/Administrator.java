package pt.ua.tqs.ecocharger.ecocharger.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("administrators")
@Table(name = "administrators")
@Data
@NoArgsConstructor
public class Administrator extends User {

    @OneToMany(mappedBy = "addedBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChargingStation> addedStations = new ArrayList<>();

    public Administrator(Long id, String email, String password, String name, boolean enabled) {
        super(id, email, password, name, enabled);
    }

    @Override
    public String toString() {
        return "Administrator{" +
                "id=" + getId() +
                ", email='" + getEmail() + '\'' +
                ", name='" + getName() + '\'' +
                ", enabled=" + isEnabled() +
                '}';
    }
}
