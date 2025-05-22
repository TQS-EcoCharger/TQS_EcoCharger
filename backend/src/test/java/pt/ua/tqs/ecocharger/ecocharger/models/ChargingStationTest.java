package pt.ua.tqs.ecocharger.ecocharger.models;

import org.junit.jupiter.api.Test;
import app.getxray.xray.junit.customjunitxml.annotations.Requirement;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;

public class ChargingStationTest {

    @Test
    @DisplayName("Test ChargingStation constructor and getters")
    @Requirement("ET-83")
    void testChargingStationConstructorAndGetters() {
        ChargingStation station = new ChargingStation(
            "Aveiro", "Rua Central", 40.6405, -8.6538, "Central St",
            "PT", "Portugal", "Electric"
        );
        station.setId(1L);

        assertEquals(1L, station.getId());
        assertEquals("Aveiro", station.getCityName());
        assertEquals("Rua Central", station.getAddress());
        assertEquals(40.6405, station.getLatitude());
        assertEquals(-8.6538, station.getLongitude());
        assertEquals("Central St", station.getStreetName());
        assertEquals("PT", station.getCountryCode());
        assertEquals("Portugal", station.getCountry());
        assertEquals("Electric", station.getVehicleType());
        assertNotNull(station.getChargingPoints());
        assertTrue(station.getChargingPoints().isEmpty());
    }

    @Test
    @DisplayName("Test ChargingStation setters")
    @Requirement("ET-83")
    void testChargingStationSetters() {
        ChargingStation station = new ChargingStation();
        station.setId(2L);
        station.setCityName("Lisboa");
        station.setAddress("Av. Liberdade");
        station.setLatitude(38.7169);
        station.setLongitude(-9.1399);
        station.setStreetName("Liberdade");
        station.setCountryCode("PT");
        station.setCountry("Portugal");
        station.setVehicleType("Hybrid");

        List<ChargingPoint> points = new ArrayList<>();
        station.setChargingPoints(points);

        assertEquals(2L, station.getId());
        assertEquals("Lisboa", station.getCityName());
        assertEquals("Av. Liberdade", station.getAddress());
        assertEquals(38.7169, station.getLatitude());
        assertEquals(-9.1399, station.getLongitude());
        assertEquals("Liberdade", station.getStreetName());
        assertEquals("PT", station.getCountryCode());
        assertEquals("Portugal", station.getCountry());
        assertEquals("Hybrid", station.getVehicleType());
        assertEquals(points, station.getChargingPoints());
    }

    @Test
    @DisplayName("Test ChargingStation toString method")
    @Requirement("ET-83")
    void testChargingStationToString() {
        ChargingStation station = new ChargingStation(
            "Porto", "Rua das Flores", 41.1496, -8.6109, "Flores",
            "PT", "Portugal", "Electric"
        );
        station.setId(3L);

        String result = station.toString();
        assertTrue(result.contains("cityName=Porto"));
        assertTrue(result.contains("address=Rua das Flores"));
        assertTrue(result.contains("country=Portugal"));
        assertTrue(result.contains("vehicleType=Electric"));
    }
}
