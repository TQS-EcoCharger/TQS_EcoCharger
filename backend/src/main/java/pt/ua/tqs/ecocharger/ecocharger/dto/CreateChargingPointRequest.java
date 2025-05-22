package pt.ua.tqs.ecocharger.ecocharger.dto;

import lombok.Data;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingPoint;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingStation;

@Data
public class CreateChargingPointRequest {
    private ChargingPoint point;
    private ChargingStation station;
}
