package pt.ua.tqs.ecocharger.ecocharger.models;

import org.junit.jupiter.api.Test;
import app.getxray.xray.junit.customjunitxml.annotations.Requirement;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;

public class ChargingPointTest {

    @Test
    @DisplayName("Test ChargingPoint constructor and getters")
    @Requirement("ET-82")
    void testChargingPointConstructorAndGetters() {
        ChargingStation station = new ChargingStation(); // você pode criar mocks se necessário
        List<Connectors> connectors = new ArrayList<>();

        ChargingPoint chargingPoint = new ChargingPoint(station, true, "Tesla", connectors);
        chargingPoint.setId(1L);
        chargingPoint.setPricePerKWh(0.3);
        chargingPoint.setPricePerMinute(0.1);

        assertEquals(1L, chargingPoint.getId());
        assertEquals(station, chargingPoint.getChargingStation());
        assertTrue(chargingPoint.isAvailable());
        assertEquals("Tesla", chargingPoint.getBrand());
        assertEquals(connectors, chargingPoint.getConnectors());
        assertEquals(0.3, chargingPoint.getPricePerKWh());
        assertEquals(0.1, chargingPoint.getPricePerMinute());
    }

    @Test
    @DisplayName("Test ChargingPoint setters")
    @Requirement("ET-82")
    void testChargingPointSetters() {
        ChargingPoint chargingPoint = new ChargingPoint();

        ChargingStation station = new ChargingStation();
        List<Connectors> connectors = new ArrayList<>();
        chargingPoint.setId(2L);
        chargingPoint.setChargingStation(station);
        chargingPoint.setAvailable(false);
        chargingPoint.setBrand("IONITY");
        chargingPoint.setConnectors(connectors);
        chargingPoint.setPricePerKWh(0.25);
        chargingPoint.setPricePerMinute(0.05);

        assertEquals(2L, chargingPoint.getId());
        assertEquals(station, chargingPoint.getChargingStation());
        assertFalse(chargingPoint.isAvailable());
        assertEquals("IONITY", chargingPoint.getBrand());
        assertEquals(connectors, chargingPoint.getConnectors());
        assertEquals(0.25, chargingPoint.getPricePerKWh());
        assertEquals(0.05, chargingPoint.getPricePerMinute());
    }

    @Test
    @DisplayName("Test ChargingPoint toString method")
    @Requirement("ET-82")
    void testChargingPointToString() {
        ChargingStation station = new ChargingStation();
        List<Connectors> connectors = new ArrayList<>();
        ChargingPoint chargingPoint = new ChargingPoint(station, true, "Tesla", connectors);
        chargingPoint.setId(1L);

        String result = chargingPoint.toString();
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("available=true"));
        assertTrue(result.contains("brand=Tesla"));
        assertTrue(result.contains("connectors=[]"));
    }
}
