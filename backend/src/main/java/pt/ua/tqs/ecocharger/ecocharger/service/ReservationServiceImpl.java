package pt.ua.tqs.ecocharger.ecocharger.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pt.ua.tqs.ecocharger.ecocharger.dto.ReservationRequestDTO;
import pt.ua.tqs.ecocharger.ecocharger.dto.ReservationResponseDTO;
import pt.ua.tqs.ecocharger.ecocharger.models.*;
import pt.ua.tqs.ecocharger.ecocharger.repository.*;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.ReservationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ChargingPointRepository chargingPointRepository;

    @Override
    public ReservationResponseDTO createReservation(ReservationRequestDTO request) {
        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        ChargingPoint chargingPoint = chargingPointRepository.findById(request.getChargingPointId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid charging point ID"));

        boolean conflict = reservationRepository.existsByChargingPointIdAndTimeOverlap(
                request.getChargingPointId(), request.getStartTime(), request.getEndTime());

        if (conflict) {
            throw new IllegalStateException("Time slot already reserved for this charging point");
        }

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setChargingPoint(chargingPoint);
        reservation.setStartTime(request.getStartTime());
        reservation.setEndTime(request.getEndTime());
        reservation.setStatus(ReservationStatus.PENDING);

        Reservation saved = reservationRepository.save(reservation);

        return mapToDTO(saved);
    }

    @Override
    public List<ReservationResponseDTO> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private ReservationResponseDTO mapToDTO(Reservation res) {
        ReservationResponseDTO dto = new ReservationResponseDTO();
        dto.setId(res.getId());
        dto.setUserId(res.getUser().getId());
        dto.setChargingPointId(res.getChargingPoint().getId());
        dto.setStartTime(res.getStartTime());
        dto.setEndTime(res.getEndTime());
        dto.setStatus(res.getStatus());
        return dto;
    }
}