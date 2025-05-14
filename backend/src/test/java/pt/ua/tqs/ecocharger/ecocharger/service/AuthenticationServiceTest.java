package pt.ua.tqs.ecocharger.ecocharger.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pt.ua.tqs.ecocharger.ecocharger.dto.AuthResultDTO;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.AuthenticationService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AuthenticationServiceTest {

    @Autowired
    private AuthenticationService authService;

    @Test
    void testSuccessfulLogin() {
        AuthResultDTO result = authService.authenticate("john@example.com", "123456");
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getMessage()).isEqualTo("Login successful");
    }

    @Test
    void testWrongPassword() {
        AuthResultDTO result = authService.authenticate("john@example.com", "wrongpass");
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getMessage()).isEqualTo("Invalid password");
    }

    @Test
    void testDisabledUser() {
        AuthResultDTO result = authService.authenticate("bob@example.com", "bobpass");
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getMessage()).isEqualTo("User is disabled");
    }

    @Test
    void testNonExistentUser() {
        AuthResultDTO result = authService.authenticate("nobody@example.com", "nopass");
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getMessage()).isEqualTo("User not found");
    }
}
