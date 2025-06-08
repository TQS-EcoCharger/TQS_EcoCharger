package pt.ua.tqs.ecocharger.ecocharger.dto;

import lombok.Data;
import lombok.Getter;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingPoint;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingStation;

@Data
@Getter
public class CreateChargingPointRequest {
  private ChargingPoint point;
  private ChargingStation station;
}
