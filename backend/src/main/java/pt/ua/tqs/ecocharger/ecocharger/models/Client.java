package pt.ua.tqs.ecocharger.ecocharger.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("clients")
@Table(name = "clients")
@Data
@NoArgsConstructor
public class Client extends User {

    public Client(Long id, String email, String password, String name, boolean enabled) {
        super(id, email, password, name, enabled);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + getId() +
                ", email='" + getEmail() + '\'' +
                ", name='" + getName() + '\'' +
                ", enabled=" + isEnabled() +
                '}';
    }
}
