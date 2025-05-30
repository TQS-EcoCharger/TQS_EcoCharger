package pt.ua.tqs.ecocharger.ecocharger.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import pt.ua.tqs.ecocharger.ecocharger.models.Administrator;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingStation;
import pt.ua.tqs.ecocharger.ecocharger.repository.AdministratorRepository;
import pt.ua.tqs.ecocharger.ecocharger.repository.ChargingStationRepository;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.AdministratorService;

@Service
public class AdministratorServiceImpl implements AdministratorService{

    private final ChargingStationRepository chargingStationRepository;
    private final AdministratorRepository administratorRepository;

    public AdministratorServiceImpl(ChargingStationRepository chargingStationRepository,
                                    AdministratorRepository administratorRepository) {
        this.chargingStationRepository = chargingStationRepository;
        this.administratorRepository = administratorRepository;
    }   

    @Override
    public ChargingStation deleteChargingStation(Long id) {
        Optional<ChargingStation> station = chargingStationRepository.findById(id);
        if (station.isPresent()) {
            ChargingStation chargingStation = station.get();
            Optional<Administrator> admin = administratorRepository.findById(station.get().getAddedBy().getId());
            if (admin.isPresent()) {
                chargingStation.setAddedBy(admin.get());
                chargingStationRepository.delete(chargingStation);
                admin.get().getAddedStations().remove(chargingStation);
                administratorRepository.save(admin.get());
                return chargingStation;
            } else {
                throw new RuntimeException("Administrator not found");
            }
        } else {
            throw new RuntimeException("Charging Station not found");
        }
    }

	@Override
	public ChargingStation assignChargingToAdmin(Long id, Long adminId) {
        Optional<ChargingStation> station = chargingStationRepository.findById(id);
        if (station.isPresent()) {
            ChargingStation chargingStation = station.get();
            Optional<Administrator> admin = administratorRepository.findById(adminId);
            if (admin.isPresent()) {
                if(chargingStation.getAddedBy() != null && chargingStation.getAddedBy().getId() == adminId) {
                    throw new RuntimeException("Charging Station already assigned to an administrator");
                } else {
                    chargingStation.setAddedBy(admin.get());
                    admin.get().getAddedStations().add(chargingStation);
                    administratorRepository.save(admin.get());
                    return chargingStationRepository.save(chargingStation);
                }
            } else {
                throw new RuntimeException("Administrator not found");
            }
        } else {
            throw new RuntimeException("Charging Station not found");
        }
    }

	@Override
	public ChargingStation unsignChargingStation(Long id, Long adminId) {
        Optional<ChargingStation> station = chargingStationRepository.findById(id);
        if (station.isPresent()) {
            ChargingStation chargingStation = station.get();
            Optional<Administrator> admin = administratorRepository.findById(adminId);
            if (admin.isPresent()) {
                if (chargingStation.getAddedBy() == null && !chargingStation.getAddedBy().getId().equals(adminId)) {
                    throw new RuntimeException("Charging Station is not assigned to this administrator");
                } else {
                    chargingStation.setAddedBy(null);
                    admin.get().getAddedStations().remove(chargingStation);
                    administratorRepository.save(admin.get());
                    return chargingStationRepository.save(chargingStation);
                }
            } else {
                throw new RuntimeException("Administrator not found");
            }
        } else {
            throw new RuntimeException("Charging Station not found");
        }
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
    

