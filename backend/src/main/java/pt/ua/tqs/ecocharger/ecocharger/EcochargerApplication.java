package pt.ua.tqs.ecocharger.ecocharger;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class EcochargerApplication {

  @PostConstruct
  void started() {
    TimeZone.setDefault(TimeZone.getTimeZone("Europe/Lisbon"));
  }

  public static void main(String[] args) {
    SpringApplication.run(EcochargerApplication.class, args);
  }
}
