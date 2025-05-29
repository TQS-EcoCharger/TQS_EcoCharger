package pt.ua.tqs.ecocharger.ecocharger.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnectorDTO {

    private Long id;
    private String connectorType;
    private Integer ratedPowerKW;
    private Integer voltageV;
    private Integer currentA;

}
