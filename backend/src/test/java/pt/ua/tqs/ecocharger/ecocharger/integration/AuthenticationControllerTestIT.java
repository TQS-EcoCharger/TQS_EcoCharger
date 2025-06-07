package pt.ua.tqs.ecocharger.ecocharger.integration;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pt.ua.tqs.ecocharger.ecocharger.config.SecurityDisableConfig;

import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(SecurityDisableConfig.class)
class AuthenticationControllerIT {

  @Autowired private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    RestAssuredMockMvc.mockMvc(mockMvc);
  }

  @Test
  @DisplayName("Register user successfully")
  void testRegisterSuccess() {
    String requestBody =
        """
        {
          "email": "testuser@example.com",
          "password": "securepass",
          "name": "Test User"
        }
        """;

    RestAssuredMockMvc.given()
        .contentType("application/json")
        .body(requestBody)
        .when()
        .post("/api/auth/register")
        .then()
        .statusCode(200)
        .body("success", equalTo(true))
        .body("token", notNullValue())
        .body("userType", equalTo("driver"));
  }

  @Test
  @DisplayName("Login with valid credentials")
  void testLoginSuccess() {
    testRegisterSuccess();

    String requestBody =
        """
        {
          "email": "testuser@example.com",
          "password": "securepass"
        }
        """;

    RestAssuredMockMvc.given()
        .contentType("application/json")
        .body(requestBody)
        .when()
        .post("/api/auth/login")
        .then()
        .statusCode(200)
        .body("success", equalTo(true))
        .body("token", notNullValue());
  }

  @Test
  @DisplayName("Login fails with wrong password")
  void testLoginFailure() {
    String requestBody =
        """
        {
          "email": "testuser@example.com",
          "password": "wrongpass"
        }
        """;

    RestAssuredMockMvc.given()
        .contentType("application/json")
        .body(requestBody)
        .when()
        .post("/api/auth/login")
        .then()
        .statusCode(401)
        .body(containsString("Invalid"));
  }

  @Test
  @DisplayName("Fetch current user with valid token")
  void testGetCurrentUser() {
    // First register and login to get token
    String registerJson =
        """
        {
          "email": "tokenuser@example.com",
          "password": "securepass",
          "name": "Token User"
        }
        """;

    String token =
        RestAssuredMockMvc.given()
            .contentType("application/json")
            .body(registerJson)
            .when()
            .post("/api/auth/register")
            .then()
            .statusCode(200)
            .extract()
            .jsonPath()
            .getString("token");

    RestAssuredMockMvc.given()
        .header("Authorization", "Bearer " + token)
        .when()
        .get("/api/auth/me")
        .then()
        .statusCode(200)
        .body("email", equalTo("tokenuser@example.com"));
  }
}
