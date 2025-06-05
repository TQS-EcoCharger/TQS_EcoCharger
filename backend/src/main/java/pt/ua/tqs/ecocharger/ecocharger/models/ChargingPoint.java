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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "charging_points")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

  public ChargingPoint(
      ChargingStation chargingStation,
      boolean available,
      String brand,
      List<Connectors> connectors) {
    this.chargingStation = chargingStation;
    this.available = available;
    this.brand = brand;
    this.connectors = connectors;
  }
}
