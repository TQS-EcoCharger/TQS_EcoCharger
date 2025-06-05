package pt.ua.tqs.ecocharger.ecocharger.service;

import java.util.List;
import java.util.Optional;

import pt.ua.tqs.ecocharger.ecocharger.models.ChargingStation;
import pt.ua.tqs.ecocharger.ecocharger.repository.ChargingStationRepository;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.ChargingStationService;
import org.springframework.stereotype.Service;
import pt.ua.tqs.ecocharger.ecocharger.utils.NotFoundException;

@Service
public class ChargingStationServiceImpl implements ChargingStationService {

  private ChargingStationRepository chargingStationRepository;

  public ChargingStationServiceImpl(ChargingStationRepository chargingStationRepository) {
    this.chargingStationRepository = chargingStationRepository;
  }

  @Override
  public ChargingStation createStation(ChargingStation station) {
    Optional<ChargingStation> existingStation =
        chargingStationRepository.findByCityName(station.getCityName());
    if (existingStation.isPresent()) {
      return existingStation.get();
    } else {
      return chargingStationRepository.save(station);
    }
  }

  @Override
  public ChargingStation updateStation(Long id, ChargingStation station) {
    Optional<ChargingStation> existingStation = chargingStationRepository.findById(id);
    if (existingStation.isPresent()) {
      ChargingStation updatedStation = existingStation.get();
      updatedStation.setCityName(station.getCityName());
      updatedStation.setAddress(station.getAddress());
      updatedStation.setLatitude(station.getLatitude());
      updatedStation.setLongitude(station.getLongitude());
      updatedStation.setStreetName(station.getStreetName());
      updatedStation.setCountryCode(station.getCountryCode());
      updatedStation.setCountry(station.getCountry());
      updatedStation.setVehicleType(station.getVehicleType());
      updatedStation.setChargingPoints(station.getChargingPoints());
      return chargingStationRepository.save(updatedStation);
    } else {
      throw new NotFoundException("Station not found");
    }
  }

  @Override
  public List<ChargingStation> getAllStationsByCityName(String cityName) {
    List<ChargingStation> stations = chargingStationRepository.findAll();
    return stations.stream()
        .filter(station -> station.getCityName().equalsIgnoreCase(cityName))
        .toList();
  }

  @Override
  public void deleteStation(Long id) {
    Optional<ChargingStation> station = chargingStationRepository.findById(id);
    if (station.isPresent()) {
      chargingStationRepository.delete(station.get());
    } else {
      throw new RuntimeException("Station not found");
    }
  }

  @Override
  public List<ChargingStation> getAllStations() {
    return chargingStationRepository.findAll();
  }
}
