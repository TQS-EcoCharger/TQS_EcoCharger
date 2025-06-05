package pt.ua.tqs.ecocharger.ecocharger.dto;

import lombok.Data;

@Data
public class StartChargingRequestDTO {
  private Long chargingPointId;
  private String otp;
  private Long carId;
}
