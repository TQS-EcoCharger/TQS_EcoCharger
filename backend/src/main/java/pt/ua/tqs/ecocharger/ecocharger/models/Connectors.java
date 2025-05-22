package pt.ua.tqs.ecocharger.ecocharger.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "connectors")
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

  public Connectors() {}

  public Connectors(
      String connectorType,
      int ratedPowerKW,
      Integer voltageV,
      Integer currentA,
      String currentType) {
    this.connectorType = connectorType;
    this.ratedPowerKW = ratedPowerKW;
    this.voltageV = voltageV;
    this.currentA = currentA;
    this.currentType = currentType;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getConnectorType() {
    return connectorType;
  }

  public void setConnectorType(String connectorType) {
    this.connectorType = connectorType;
  }

  public int getRatedPowerKW() {
    return ratedPowerKW;
  }

  public void setRatedPowerKW(int ratedPowerKW) {
    this.ratedPowerKW = ratedPowerKW;
  }

  public Integer getVoltageV() {
    return voltageV;
  }

  public void setVoltageV(Integer voltageV) {
    this.voltageV = voltageV;
  }

  public Integer getCurrentA() {
    return currentA;
  }

  public void setCurrentA(Integer currentA) {
    this.currentA = currentA;
  }

  public String getCurrentType() {
    return currentType;
  }

  public void setCurrentType(String currentType) {
    this.currentType = currentType;
  }

  public ChargingPoint getChargingPoint() {
    return chargingPoint;
  }

  public void setChargingPoint(ChargingPoint chargingPoint) {
    this.chargingPoint = chargingPoint;
  }

  @Override
  public String toString() {
    return "Connectors [id="
        + id
        + ", connectorType="
        + connectorType
        + ", ratedPowerKW="
        + ratedPowerKW
        + ", voltageV="
        + voltageV
        + ", currentA="
        + currentA
        + ", currentType="
        + currentType
        + ", chargingPoint="
        + chargingPoint
        + "]";
  }
}
