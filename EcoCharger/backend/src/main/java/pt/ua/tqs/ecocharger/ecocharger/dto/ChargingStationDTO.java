package pt.ua.tqs.ecocharger.ecocharger.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChargingStationDTO {
  private Long id;
  private String cityName;
  private String address;
  private Double latitude;
  private Double longitude;
}
