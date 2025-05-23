package pt.ua.tqs.ecocharger.ecocharger.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
@Getter
@AllArgsConstructor
public class ReservationRequestDTO {
    private Long userId;
    private Long chargingPointId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
