package pt.ua.tqs.ecocharger.ecocharger.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import app.getxray.xray.junit.customjunitxml.annotations.Requirement;
import pt.ua.tqs.ecocharger.ecocharger.models.Administrator;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingStation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ChargingStationRepositoryTest {

  @Autowired private ChargingStationRepository chargingStationRepository;
  @Autowired private AdministratorRepository administratorRepository;

  private ChargingStation createStation(String city) {
    ChargingStation station = new ChargingStation(city, "Rua X", 40.6, -8.6, "PT", "Portugal");
    return chargingStationRepository.save(station);
  }

  private ChargingStation createStationWithAdmin(String city) {
    Administrator admin =
        new Administrator(null, UUID.randomUUID() + "@admin.com", "adminpass", "Admin", true);
    admin = administratorRepository.save(admin);
    ChargingStation station = new ChargingStation(city, "Rua Admin", 41.0, -8.5, "PT", "Portugal");
    station.setAddedBy(admin);
    return chargingStationRepository.save(station);
  }

  @Test
  @Requirement("ET-18")
  @DisplayName("Save and retrieve a charging station")
  void testSaveAndFind() {
    ChargingStation station = createStation("Lisbon");

    Optional<ChargingStation> found = chargingStationRepository.findById(station.getId());

    assertThat(found).isPresent();
    assertThat(found.get().getCityName()).isEqualTo("Lisbon");
  }

  @Test
  @Requirement("ET-18")
  @DisplayName("Find charging stations by city name")
  void testFindByCityName() {
    createStation("Porto");
    createStation("Porto");

    Optional<List<ChargingStation>> found = chargingStationRepository.findByCityName("Porto");

    assertThat(found).isPresent();
    assertThat(found.get()).hasSizeGreaterThanOrEqualTo(2);
    assertThat(found.get().get(0).getCityName()).isEqualTo("Porto");
  }

  @Test
  @Requirement("ET-18")
  @DisplayName("Find all charging stations")
  void testFindAllStations() {
    createStation("Faro");
    createStation("Braga");

    List<ChargingStation> stations = chargingStationRepository.findAll();

    assertThat(stations).hasSizeGreaterThanOrEqualTo(2);
  }

  @Test
  @Requirement("ET-18")
  @DisplayName("Charging station should persist associated admin")
  void testStationWithAdmin() {
    ChargingStation station = createStationWithAdmin("Coimbra");

    Optional<ChargingStation> found = chargingStationRepository.findById(station.getId());

    assertThat(found).isPresent();
    assertThat(found.get().getAddedBy()).isNotNull();
    assertThat(found.get().getAddedBy().getName()).isEqualTo("Admin");
  }
}
