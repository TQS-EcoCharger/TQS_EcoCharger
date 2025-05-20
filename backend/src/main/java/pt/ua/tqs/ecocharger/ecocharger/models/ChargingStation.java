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

    @Column(name="municipality",nullable = false)
    private String cityName;

    @Column(name = "address",nullable = false)
    private String address;

    @Column(name = "latitude",nullable = false)
    private double latitude;

    @Column(name = "longitude",nullable = false)
    private double longitude;

    @Column(name = "streetNumber", nullable = false)
    private String streetNumber;

    @Column(name = "streetName", nullable = false)
    private String streetName;

    @Column(name = "countryCode", nullable = false)
    private String countryCode;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "veicleType", nullable = false)
    private List<String> veicleType;

    @OneToMany(mappedBy = "chargingStation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChargingPoint> chargingPoints = new ArrayList<>();

    public ChargingStation() {
    }

    public ChargingStation(String cityName, String address, double latitude, double longitude, String streetNumber,
            String streetName, String countryCode, String country, List<String> veicleType) {
        this.cityName = cityName;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.streetNumber = streetNumber;
        this.streetName = streetName;
        this.countryCode = countryCode;
        this.country = country;
        this.veicleType = veicleType;
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

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
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

    public List<String> getVeicleType() {
        return veicleType;
    }

    public void setVeicleType(List<String> veicleType) {
        this.veicleType = veicleType;
    }

    public List<ChargingPoint> getChargingPoints() {
        return chargingPoints;
    }

    public void setChargingPoints(List<ChargingPoint> chargingPoints) {
        this.chargingPoints = chargingPoints;
    }


    @Override
    public String toString() {
        return "ChargingStation [id=" + id + ", cityName=" + cityName + ", address=" + address + ", latitude="
                + latitude + ", longitude=" + longitude + ", streetNumber=" + streetNumber + ", streetName="
                + streetName + ", countryCode=" + countryCode + ", country=" + country + ", veicleType=" + veicleType
                + ", chargingPoints=" + chargingPoints + "]";
    }

    
}
