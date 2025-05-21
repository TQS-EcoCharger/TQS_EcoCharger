package pt.ua.tqs.ecocharger.ecocharger.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "charging_points")
public class ChargingPoint {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "station_id", nullable = false)
    private ChargingStation chargingStation;

    @Column(name = "available", nullable = false)
    private boolean available;

    @Column(name = "brand", nullable = false)
    private String brand;

    @OneToMany(mappedBy = "chargingPoint", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Connectors> connectors = new ArrayList<>();

    @Column(name = "price_per_kwh") 
    private Double pricePerKWh;

    @Column(name = "price_per_minute")
    private Double pricePerMinute;

    
    public ChargingPoint() {
    }

    public ChargingPoint(ChargingStation chargingStation, boolean available, String brand, List<Connectors> connectors) {
        this.chargingStation = chargingStation;
        this.available = available;
        this.brand = brand;
        this.connectors = connectors;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ChargingStation getChargingStation() {
        return chargingStation;
    }

    public void setChargingStation(ChargingStation chargingStation) {
        this.chargingStation = chargingStation;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public List<Connectors> getConnectors() {
        return connectors;
    }

    public void setConnectors(List<Connectors> connectors) {
        this.connectors = connectors;
    }

    public Double getPricePerKWh() {
        return pricePerKWh;
    }

    public void setPricePerKWh(Double pricePerKWh) {
        this.pricePerKWh = pricePerKWh;
    }

    public Double getPricePerMinute() {
        return pricePerMinute;
    }

    public void setPricePerMinute(Double pricePerMinute) {
        this.pricePerMinute = pricePerMinute;
    }

    @Override
    public String toString() {
        return "ChargingPoint [id=" + id + ", chargingStation=" + chargingStation + ", available=" + available
                + ", brand=" + brand + ", connectors=" + connectors + "]";
    }
    
}


    
