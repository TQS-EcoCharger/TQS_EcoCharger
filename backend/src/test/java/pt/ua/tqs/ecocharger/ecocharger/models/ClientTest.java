package pt.ua.tqs.ecocharger.ecocharger.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import app.getxray.xray.junit.customjunitxml.annotations.Requirement;

class ClientTest {

  @Test
  @DisplayName("Test Client constructor with parameters")
  @Requirement("ET-20")
  void testClientConstructorWithParams() {

    Long id = 1L;
    String email = "client@example.com";
    String password = "securePass";
    String name = "Joana";
    boolean enabled = true;

    Client client = new Client(id, email, password, name, enabled);

    assertEquals(id, client.getId());
    assertEquals(email, client.getEmail());
    assertEquals(password, client.getPassword());
    assertEquals(name, client.getName());
    assertTrue(client.isEnabled());
  }

  @Test
  @DisplayName("Test Client no-args constructor")
  @Requirement("ET-20")
  void testClientNoArgsConstructor() {
    Client client = new Client();

    assertNull(client.getId());
    assertNull(client.getEmail());
    assertNull(client.getPassword());
    assertNull(client.getName());
  }

  @Test
  @DisplayName("Test Client toString method")
  @Requirement("ET-20")
  void testToString() {
    Client client = new Client(2L, "ana@example.com", "123456", "Ana", true);
    String toString = client.toString();

    assertTrue(toString.contains("Client{"));
    assertTrue(toString.contains("ana@example.com"));
    assertTrue(toString.contains("Ana"));
  }
}
