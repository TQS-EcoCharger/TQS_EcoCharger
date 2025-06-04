package pt.ua.tqs.ecocharger.ecocharger.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pt.ua.tqs.ecocharger.ecocharger.models.ReservationStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponseDTO {

  private Long id;
  private Long userId;
  private ChargingPointDTO chargingPoint;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private ReservationStatus status;
}
