package pt.ua.tqs.ecocharger.ecocharger.models;

import org.junit.jupiter.api.Test;
import app.getxray.xray.junit.customjunitxml.annotations.Requirement;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;

class AdministratorTest {

    @Test
    @DisplayName("Test Administrator constructor, getters, and default state")
    @Requirement("ET-20")
    void testConstructorAndDefaultState() {
        Long id = 1L;
        String email = "admin@example.com";
        String password = "securepassword";
        String name = "Admin User";
        boolean enabled = true;

        Administrator admin = new Administrator(id, email, password, name, enabled);

        assertEquals(id, admin.getId());
        assertEquals(email, admin.getEmail());
        assertEquals(password, admin.getPassword());
        assertEquals(name, admin.getName());
        assertTrue(admin.isEnabled());

        assertNotNull(admin.getAddedStations());
        assertTrue(admin.getAddedStations().isEmpty());
    }

    @Test
    @DisplayName("Test adding and removing ChargingStation")
    @Requirement("ET-20")
    void testManagingChargingStations() {
        Administrator admin = new Administrator(2L, "admin2@example.com", "1234", "Manager", true);

        ChargingStation station1 = new ChargingStation();
        station1.setId(10L);
        station1.setCityName("Porto");
        station1.setAddress("Rua A");
        station1.setAddedBy(admin); 

        ChargingStation station2 = new ChargingStation();
        station2.setId(11L);
        station2.setCityName("Lisboa");
        station2.setAddress("Rua B");
        station2.setAddedBy(admin);

        admin.getAddedStations().add(station1);
        admin.getAddedStations().add(station2);

        assertEquals(2, admin.getAddedStations().size());
        assertTrue(admin.getAddedStations().contains(station1));
        assertTrue(admin.getAddedStations().contains(station2));

        admin.getAddedStations().remove(station1);
        assertEquals(1, admin.getAddedStations().size());
        assertFalse(admin.getAddedStations().contains(station1));
    }

    @Test
    @DisplayName("Test toString output contains meaningful information")
    @Requirement("ET-20")
    void testToString() {
        Administrator admin = new Administrator(3L, "admin3@example.com", "pass", "Test Admin", true);
        String output = admin.toString();

        assertTrue(output.contains("Administrator"));
        assertTrue(output.contains("admin3@example.com"));
        assertTrue(output.contains("Test Admin"));
        assertTrue(output.contains("true"));
    }
}
