package pt.ua.tqs.ecocharger.ecocharger.models;

import java.time.Year;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Car model stored in the DB <br>
 * Associated with a driver
 */
@Entity
@Table(name = "car")
@Getter
@EqualsAndHashCode
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

    /**
     * Battery capacity in kWh (not percentage)
     */
    @Column(nullable = false)
    private Double batteryCapacity;

    /**
     * Battery level in kWh (not percentage)
     */
    @Column(nullable = false)
    private Double batteryLevel;

    @Column(nullable = false)
    private Double kilometers;

    @Column(nullable = false)
    private Double consumption;

    @Column(nullable = false)
    private boolean enabled;

    public Car() {
        this.id = null;
        this.name = "";
        this.make = "";
        this.model = "";
        this.year = Year.now().getValue();
        this.licensePlate = "";
        this.batteryCapacity = 0.0;
        this.batteryLevel = 0.0;
        this.kilometers = 0.0;
        this.consumption = 0.0;
        this.enabled = false;
    }

    public Car(Long id, String name, String make, String model, Integer year, String licensePlate,
            Double batteryCapacity, Double batteryLevel, Double kilometers, Double consumption) {
        try {
            validateCarData(name, make, model, year, licensePlate, batteryCapacity, batteryLevel, kilometers, consumption);
            this.id = id;
            this.name = name;
            this.make = make;
            this.model = model;
            this.year = year;
            this.licensePlate = licensePlate;
            this.batteryCapacity = batteryCapacity;
            this.batteryLevel = batteryLevel;
            this.kilometers = kilometers;
            this.consumption = consumption;
            this.enabled = true;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid car data: " + e.getMessage());
        }
        
    }

    public void setId(Long id) {
        if (validateId(id)) {
            this.id = id;
        } else {
            throw new IllegalArgumentException("ID must be a positive number");
        }
    }

    public boolean validateId(Long id) {
        return id != null && id > 0;
    }


    public void setName(String name) {
        if (validateName(name)) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
    }

    public boolean validateName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    public void setMake(String make) {
        if (validateMake(make)) {
            this.make = make;
        } else {
            throw new IllegalArgumentException("Make cannot be null or empty");
        }
    }

    public boolean validateMake(String make) {
        return make != null && !make.trim().isEmpty();
    }

    public void setModel(String model) {
        if (validateModel(model)) {
            this.model = model;
        } else {
            throw new IllegalArgumentException("Model cannot be null or empty");
        }
    }

    public boolean validateModel(String model) {
        return model != null && !model.trim().isEmpty();
    }

    public void setYear(Integer year) {
        if (validateYear(year)) {
            this.year = year;
        } else {
            throw new IllegalArgumentException("Year must be a valid number greater than 0");
        }
    }

    public boolean validateYear(Integer year) {
        return year != null && year > 0;
    }

    public void setLicensePlate(String licensePlate) {
        if (validateLicensePlate(licensePlate)) {
            this.licensePlate = sanitizeLicensePlate(licensePlate);
        } else {
            throw new IllegalArgumentException("Invalid license plate format");
        }
    }

    /**
     * Sanitizes the license plate format. <br>
     * If it has no dashes, it adds them in the format AB-12-CD <br>
     * If it has dashes, it removes all spaces and makes it uppercase <br>
     * @param licensePlate
     * @return sanitized license plate
     */
    public String sanitizeLicensePlate(String licensePlate) {
        if (licensePlate == null) {
            return null;
        }
        licensePlate = licensePlate.toUpperCase().replaceAll(" ", "");
        if (!licensePlate.contains("-")) {
            return licensePlate.replaceAll("(.{2})(.{2})(.{2})", "$1-$2-$3");
        }
        return licensePlate;
    }

    /**
     * Validates the license plate format. <br>
     * Example: AB-12-CD <br>
     * Supposed to support foreign license plates
     * 
     * 
     * @param licensePlate
     * @return true if valid, false otherwise
     */
    private boolean validateLicensePlate(String licensePlate) {
        return licensePlate != null && licensePlate.matches("^[A-Z0-9]{1,4}([ -]?[A-Z0-9]{1,4}){0,2}$");
    }

    public void setBatteryCapacity(Double batteryCapacity) {
        if (validateBatteryCapacity(batteryCapacity)) {
            this.batteryCapacity = batteryCapacity;
        } else {
            throw new IllegalArgumentException("Battery capacity must be a positive number");
        }
    }

    private boolean validateBatteryCapacity(Double batteryCapacity) {
        return batteryCapacity != null && batteryCapacity > 0;
    }

    public void setBatteryLevel(Double batteryLevel) {
        if (validateBatteryLevel(batteryLevel, this.batteryCapacity)) {
            this.batteryLevel = batteryLevel;
        } else {
            throw new IllegalArgumentException("Battery level must be between 0 and the battery capacity");
        }
    }

    private boolean validateBatteryLevel(Double batteryLevel, Double batteryCapacity) {
        return batteryLevel != null && batteryLevel >= 0 && batteryLevel <= batteryCapacity;
    }

    public void setKilometers(Double kilometers) {
        if (validateKilometers(kilometers)) {
            this.kilometers = kilometers;
        } else {
            throw new IllegalArgumentException("Kilometers must be a positive number");
        }
    }

    private boolean validateKilometers(Double kilometers) {
        return kilometers != null && kilometers >= 0;
    }

    public void setConsumption(Double consumption) {
        if (validateConsumption(consumption)) {
            this.consumption = consumption;
        } else {
            throw new IllegalArgumentException("Consumption must be a positive number");
        }
    }

    private boolean validateConsumption(Double consumption) {
        return consumption != null && consumption > 0;
    }

    public boolean setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this.enabled;
    }

    private void validateCarData(String name, String make, String model, Integer year,
                                String licensePlate, Double batteryCapacity, Double batteryLevel,
                                Double kilometers, Double consumption) {

        if (!validateName(name)) throw new IllegalArgumentException("Invalid name");
        if (!validateMake(make)) throw new IllegalArgumentException("Invalid make");
        if (!validateModel(model)) throw new IllegalArgumentException("Invalid model");
        if (!validateYear(year)) throw new IllegalArgumentException("Invalid year");
        if (!validateLicensePlate(licensePlate)) throw new IllegalArgumentException("Invalid license plate");
        if (!validateBatteryCapacity(batteryCapacity)) throw new IllegalArgumentException("Invalid battery capacity");
        if (!validateBatteryLevel(batteryLevel, batteryCapacity)) throw new IllegalArgumentException("Invalid battery level");
        if (!validateKilometers(kilometers)) throw new IllegalArgumentException("Invalid kilometers");
        if (!validateConsumption(consumption)) throw new IllegalArgumentException("Invalid consumption");
    }

    
}
