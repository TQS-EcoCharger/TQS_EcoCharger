package pt.ua.tqs.ecocharger.ecocharger.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pt.ua.tqs.ecocharger.ecocharger.models.ReservationStatus;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponseDTO {
    private Long id;
    private Long userId;
    private Long chargingPointId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ReservationStatus status;
}