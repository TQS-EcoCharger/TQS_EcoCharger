package pt.ua.tqs.ecocharger.ecocharger.service.interfaces;

import pt.ua.tqs.ecocharger.ecocharger.dto.ReservationRequestDTO;
import pt.ua.tqs.ecocharger.ecocharger.dto.ReservationResponseDTO;

import java.util.List;

public interface ReservationService {
    ReservationResponseDTO createReservation(ReservationRequestDTO request);
    List<ReservationResponseDTO> getAllReservations();
    List<ReservationResponseDTO> getReservationsByUserId(Long userId);
    List<ReservationResponseDTO> getActiveReservationsByChargingPointId(Long chargingPointId);


}
