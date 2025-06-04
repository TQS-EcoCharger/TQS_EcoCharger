package pt.ua.tqs.ecocharger.ecocharger.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChargingPointDTO {

    private Long id;
    private String brand;
    private boolean available;
    private Double pricePerKWh;
    private Double pricePerMinute;
    private List<ConnectorDTO> connectors;
    private ChargingStationDTO chargingStation; 
}
