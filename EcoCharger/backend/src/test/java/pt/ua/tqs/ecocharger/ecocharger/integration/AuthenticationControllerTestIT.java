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

import static org.hamcrest.Matchers.*;

import app.getxray.xray.junit.customjunitxml.annotations.Requirement;
import pt.ua.tqs.ecocharger.ecocharger.config.SecurityDisableConfig;

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

  private String generateUniqueEmail(String prefix) {
    return prefix + "_" + System.currentTimeMillis() + "@example.com";
  }

  @Test
  @Requirement("ET-52")
  @DisplayName("Register user successfully")
  void testRegisterSuccess() {
    String email = generateUniqueEmail("register");

    String requestBody =
        """
        {
          "email": "%s",
          "password": "securepass",
          "name": "Test User",
          "userType": "driver"
        }
        """
            .formatted(email);

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
  @Requirement("ET-49")
  @DisplayName("Login with valid credentials")
  void testLoginSuccess() {
    String email = generateUniqueEmail("login");

    String registerRequest =
        """
        {
          "email": "%s",
          "password": "securepass",
          "name": "Test User",
          "userType": "driver"
        }
        """
            .formatted(email);

    RestAssuredMockMvc.given()
        .contentType("application/json")
        .body(registerRequest)
        .when()
        .post("/api/auth/register")
        .then()
        .statusCode(200);

    String loginRequest =
        """
        {
          "email": "%s",
          "password": "securepass"
        }
        """
            .formatted(email);

    RestAssuredMockMvc.given()
        .contentType("application/json")
        .body(loginRequest)
        .when()
        .post("/api/auth/login")
        .then()
        .statusCode(200)
        .body("success", equalTo(true))
        .body("token", notNullValue());
  }

  @Test
  @DisplayName("Login fails with wrong password")
  @Requirement("ET-49")
  void testLoginFailure() {
    String email = generateUniqueEmail("fail");

    String registerRequest =
        """
        {
          "email": "%s",
          "password": "correctpass",
          "name": "Fail User",
          "userType": "driver"
        }
        """
            .formatted(email);

    RestAssuredMockMvc.given()
        .contentType("application/json")
        .body(registerRequest)
        .when()
        .post("/api/auth/register")
        .then()
        .statusCode(200);

    String badLoginRequest =
        """
        {
          "email": "%s",
          "password": "wrongpass"
        }
        """
            .formatted(email);

    RestAssuredMockMvc.given()
        .contentType("application/json")
        .body(badLoginRequest)
        .when()
        .post("/api/auth/login")
        .then()
        .statusCode(401)
        .body(containsString("Invalid"));
  }

  @Test
  @DisplayName("Fetch current user with valid token")
  @Requirement("ET-49")
  void testGetCurrentUser() {
    String email = generateUniqueEmail("token");

    String registerJson =
        """
        {
          "email": "%s",
          "password": "securepass",
          "name": "Token User",
          "userType": "driver"
        }
        """
            .formatted(email);

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
        .body("email", equalTo(email));
  }
}
