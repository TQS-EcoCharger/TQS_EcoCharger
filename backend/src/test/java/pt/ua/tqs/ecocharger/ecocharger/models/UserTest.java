package pt.ua.tqs.ecocharger.ecocharger.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    void testUserConstructorAndGetters() {
        User user = new User(1L, "john@example.com", "password123", "John Doe", true);

        assertEquals(1L, user.getId());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals("John Doe", user.getName());
        assertTrue(user.isEnabled());
    }
    @Test
    void testUserSetters() {
        User user = new User();
        user.setId(2L);
        user.setEmail("jane@example.com");
        user.setPassword("pass456");
        user.setName("Jane Doe");
        user.setEnabled(false);

        assertEquals(2L, user.getId());
        assertEquals("jane@example.com", user.getEmail());
        assertEquals("pass456", user.getPassword());
        assertEquals("Jane Doe", user.getName());
        assertFalse(user.isEnabled());
    }

    @Test
    void testUserEquality() {
        User user1 = new User(1L, "a@b.com", "pass", "Name", true);
        User user2 = new User(1L, "a@b.com", "pass", "Name", true);

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }
}
