package pt.ua.tqs.ecocharger.ecocharger.models;

import java.util.ArrayList;
import java.util.List;

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

  public ChargingStation() {}

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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCityName() {
    return cityName;
  }

  public void setCityName(String cityName) {
    this.cityName = cityName;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public String getStreetName() {
    return streetName;
  }

  public void setStreetName(String streetName) {
    this.streetName = streetName;
  }

  public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getVehicleType() {
    return vehicleType;
  }

  public void setVehicleType(String vehicleType) {
    this.vehicleType = vehicleType;
  }

  public List<ChargingPoint> getChargingPoints() {
    return chargingPoints;
  }

  public void setChargingPoints(List<ChargingPoint> chargingPoints) {
    this.chargingPoints = chargingPoints;
  }

  @Override
  public String toString() {
    return "ChargingStation [id="
        + id
        + ", cityName="
        + cityName
        + ", address="
        + address
        + ", latitude="
        + latitude
        + ", longitude="
        + longitude
        + ", streetName="
        + streetName
        + ", countryCode="
        + countryCode
        + ", country="
        + country
        + ", vehicleType="
        + vehicleType
        + ", chargingPoints="
        + chargingPoints
        + "]";
  }
}
