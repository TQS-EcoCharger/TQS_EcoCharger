package pt.ua.tqs.ecocharger.ecocharger.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChargingPointDTO {

  private Long id;
  private String brand;
  private boolean available;
  private Double pricePerKWh;
  private Double pricePerMinute;
  private Double chargingRateKWhPerMinute;
  private List<ConnectorDTO> connectors;

  @JsonProperty("chargingStation")
  private ChargingStationDTO chargingStation;
}
