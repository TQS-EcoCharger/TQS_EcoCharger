package pt.ua.tqs.ecocharger.ecocharger.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pt.ua.tqs.ecocharger.ecocharger.models.Car;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingStatus;
import pt.ua.tqs.ecocharger.ecocharger.models.User;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChargingSessionResponseDTO {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long durationMinutes;
    private Double totalCost;
    private ChargingStatus status;
    private Double initialBatteryLevel;
    private Double energyDelivered;
    private ChargingPointDTO chargingPoint;
    private User user;
    private Car car;
}
