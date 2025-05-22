package pt.ua.tqs.ecocharger.ecocharger.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Entity
@Table(name = "connectors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Connectors {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "connector_type", nullable = false)
  private String connectorType;

  @Column(name = "rated_power_kw", nullable = false)
  private int ratedPowerKW;

  @Column(name = "voltage_v")
  private Integer voltageV;

  @Column(name = "current_a")
  private Integer currentA;

  @Column(name = "current_type", nullable = false)
  private String currentType;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "charging_point_id")
  private ChargingPoint chargingPoint;

  public Connectors(String connectorType, int ratedPowerKW, Integer voltageV, Integer currentA, String currentType) {
    this.connectorType = connectorType;
    this.ratedPowerKW = ratedPowerKW;
    this.voltageV = voltageV;
    this.currentA = currentA;
    this.currentType = currentType;
  }
}