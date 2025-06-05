package pt.ua.tqs.ecocharger.ecocharger.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActiveSessionDTO {
    private Long sessionId;
    private Long chargingPointId;
    private Long carId;
    private String carName;
    private long durationMinutes;
    private double currentBatteryLevel;
    private double energyDelivered;
    private double totalCost;
}
