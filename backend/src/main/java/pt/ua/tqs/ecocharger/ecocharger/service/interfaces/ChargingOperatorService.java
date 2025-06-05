package pt.ua.tqs.ecocharger.ecocharger.service.interfaces;

import pt.ua.tqs.ecocharger.ecocharger.models.ChargingOperator;

public interface ChargingOperatorService {

    ChargingOperator getChargingOperatorById(Long id);

    boolean chargingOperatorExists(Long id);
    
}
