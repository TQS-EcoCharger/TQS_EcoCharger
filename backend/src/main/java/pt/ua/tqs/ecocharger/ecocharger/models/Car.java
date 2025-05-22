package pt.ua.tqs.ecocharger.ecocharger.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Car model stored in the DB <br>
 * Associated with a driver
 */
@Entity
@Table(name = "car")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String make;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private String licensePlate;

    @Column(nullable = false)
    private Double batteryCapacity;

    @Column(nullable = false)
    private Double batteryLevel;

    @Column(nullable = false)
    private Double kilometers;

    @Column(nullable = false)
    private Double consumption;

    public void setLicensePlate(String licensePlate) {
        if (validateLicensePlate(licensePlate)) {
            this.licensePlate = licensePlate;
        } else {
            throw new IllegalArgumentException("Invalid license plate format");
        }
    }

    private boolean validateLicensePlate(String licensePlate) {
        // Implement your validation logic here
        // For example, check if the license plate matches a specific pattern
        return licensePlate.matches("[A-Z]{2}-\\d{2}-[A-Z]{2}");
    }

    
}
