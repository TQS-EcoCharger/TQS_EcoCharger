package pt.ua.tqs.ecocharger.ecocharger.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;

import app.getxray.xray.junit.customjunitxml.annotations.Requirement;
import org.junit.jupiter.api.Test;

public class ConnectorsTest {
    
    @Test
    @DisplayName("Test Connectors constructor and basic field values")
    @Requirement("ET-83")
    void testConstructor() {
        Connectors connector = new Connectors(
            "Type2", 22, 400, 32, "AC"
        );

        assertEquals("Type2", connector.getConnectorType());
        assertEquals(22, connector.getRatedPowerKW());
        assertEquals(400, connector.getVoltageV());
        assertEquals(32, connector.getCurrentA());
        assertEquals("AC", connector.getCurrentType());
    }

}
