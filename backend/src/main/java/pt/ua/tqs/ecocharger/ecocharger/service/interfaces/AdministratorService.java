package pt.ua.tqs.ecocharger.ecocharger.service.interfaces;

import pt.ua.tqs.ecocharger.ecocharger.models.Administrator;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingPoint;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingStation;

public interface AdministratorService {
    ChargingStation assignChargingToAdmin(Long id, Long adminId);
    ChargingStation unsignChargingStation(Long id, Long adminId);
    ChargingStation updateChargingStation(ChargingStation station);
    ChargingStation deleteChargingStation(Long id);
    Administrator createAdministrator(String email, String password, String name);
    ChargingPoint updateChargingPoint(ChargingPoint point, Long pointId);
}
