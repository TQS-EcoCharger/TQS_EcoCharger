package pt.ua.tqs.ecocharger.ecocharger.models;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "charging_stations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChargingStation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "municipality", nullable = false)
  private String cityName;

  @Column(name = "address", nullable = false)
  private String address;

  @Column(name = "latitude", nullable = false)
  private double latitude;

  @Column(name = "longitude", nullable = false)
  private double longitude;

  @Column(name = "streetname", nullable = false)
  private String streetName;

  @Column(name = "countrycode", nullable = false)
  private String countryCode;

  @Column(name = "country", nullable = false)
  private String country;

  @Column(name = "vehicletype", nullable = false)
  private String vehicleType;

  @OneToMany(mappedBy = "chargingStation", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ChargingPoint> chargingPoints = new ArrayList<>();


  public ChargingStation(
      String cityName,
      String address,
      double latitude,
      double longitude,
      String streetName,
      String countryCode,
      String country,
      String vehicleType) {
    this.cityName = cityName;
    this.address = address;
    this.latitude = latitude;
    this.longitude = longitude;
    this.streetName = streetName;
    this.countryCode = countryCode;
    this.country = country;
    this.vehicleType = vehicleType;
  }
}