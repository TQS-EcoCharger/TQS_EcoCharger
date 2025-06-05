package pt.ua.tqs.ecocharger.ecocharger.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import pt.ua.tqs.ecocharger.ecocharger.models.Administrator;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingPoint;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingStation;
import pt.ua.tqs.ecocharger.ecocharger.models.Connectors;
import pt.ua.tqs.ecocharger.ecocharger.repository.AdministratorRepository;
import pt.ua.tqs.ecocharger.ecocharger.repository.ChargingPointRepository;
import pt.ua.tqs.ecocharger.ecocharger.repository.ChargingStationRepository;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.AdministratorService;

@Service
public class AdministratorServiceImpl implements AdministratorService {

  private final ChargingStationRepository chargingStationRepository;
  private final AdministratorRepository administratorRepository;
  private final ChargingPointRepository chargingPointRepository;

  public AdministratorServiceImpl(
      ChargingStationRepository chargingStationRepository,
      AdministratorRepository administratorRepository,
      ChargingPointRepository chargingPointRepository) {
    this.chargingStationRepository = chargingStationRepository;
    this.administratorRepository = administratorRepository;
    this.chargingPointRepository = chargingPointRepository;
  }

@Override
public ChargingStation deleteChargingStation(Long id) {
    ChargingStation station = chargingStationRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Charging Station not found"));

    station.getChargingPoints().clear();

    Administrator addedBy = station.getAddedBy();
    if (addedBy != null) {
        addedBy.getAddedStations().remove(station);
        administratorRepository.save(addedBy);
    }

    chargingStationRepository.delete(station);
    return station;
}


  @Override
  public ChargingStation updateChargingStation(ChargingStation station) {
    Optional<ChargingStation> existingStation = chargingStationRepository.findById(station.getId());
    if (existingStation.isPresent()) {
      ChargingStation updatedStation = existingStation.get();
      updatedStation.setCityName(station.getCityName());
      updatedStation.setAddress(station.getAddress());
      updatedStation.setLatitude(station.getLatitude());
      updatedStation.setLongitude(station.getLongitude());
      updatedStation.setAddedBy(station.getAddedBy());
      updatedStation.setCountryCode(station.getCountryCode());
      updatedStation.setCountry(station.getCountry());
      return chargingStationRepository.save(updatedStation);
    } else {
      throw new RuntimeException("Charging Station not found");
    }
  }

  @Override
  public ChargingPoint updateChargingPoint(ChargingPoint point, Long pointId) {
    Optional<ChargingPoint> existingPointOpt = chargingPointRepository.findById(pointId);
    if (existingPointOpt.isEmpty()) {
      throw new RuntimeException("Charging Point not found");
    }

    ChargingPoint existingPoint = existingPointOpt.get();

    existingPoint.setChargingStation(point.getChargingStation());
    existingPoint.setAvailable(point.isAvailable());
    existingPoint.setBrand(point.getBrand());
    existingPoint.setPricePerKWh(point.getPricePerKWh());
    existingPoint.setPricePerMinute(point.getPricePerMinute());

    existingPoint.getConnectors().clear();
    for (Connectors connector : point.getConnectors()) {
      connector.setChargingPoint(existingPoint);
      existingPoint.getConnectors().add(connector);
    }

    return chargingPointRepository.save(existingPoint);
  }

  @Override
  public Administrator createAdministrator(String email, String password, String name) {
    Optional<Administrator> existingAdmin = administratorRepository.findByEmail(email);
    if (existingAdmin.isPresent()) {
      throw new RuntimeException("Administrator with this email already exists");
    }
    Administrator newAdmin = new Administrator();
    newAdmin.setEmail(email);
    newAdmin.setPassword(password);
    newAdmin.setName(name);
    return administratorRepository.save(newAdmin);
  }
}
