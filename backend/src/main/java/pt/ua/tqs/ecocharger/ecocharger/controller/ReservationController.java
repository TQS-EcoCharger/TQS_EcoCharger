package pt.ua.tqs.ecocharger.ecocharger.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ua.tqs.ecocharger.ecocharger.dto.ReservationRequestDTO;
import pt.ua.tqs.ecocharger.ecocharger.dto.ReservationResponseDTO;
import pt.ua.tqs.ecocharger.ecocharger.models.*;

import pt.ua.tqs.ecocharger.ecocharger.repository.UserRepository;
import pt.ua.tqs.ecocharger.ecocharger.repository.ChargingPointRepository;
import pt.ua.tqs.ecocharger.ecocharger.repository.ReservationRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ChargingPointRepository chargingPointRepository;

    @PostMapping
    public ResponseEntity<Object> createReservation(@RequestBody ReservationRequestDTO request) {
        if (request.getStartTime().isAfter(request.getEndTime())) {
            return ResponseEntity.badRequest().body("Start time must be before end time");
        }

        User user = userRepository.findById(request.getUserId())
                .orElse(null);
        ChargingPoint chargingPoint = chargingPointRepository.findById(request.getChargingPointId())
                .orElse(null);

        if (user == null || chargingPoint == null) {
            return ResponseEntity.badRequest().body("Invalid user or charging point ID");
        }

        boolean conflict = reservationRepository.existsByChargingPointIdAndTimeOverlap(
                request.getChargingPointId(), request.getStartTime(), request.getEndTime());

        if (conflict) {
            return ResponseEntity.status(409).body("Time slot already reserved for this charging point");
        }

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setChargingPoint(chargingPoint);
        reservation.setStartTime(request.getStartTime());
        reservation.setEndTime(request.getEndTime());
        reservation.setStatus(ReservationStatus.PENDING);

        Reservation saved = reservationRepository.save(reservation);

        ReservationResponseDTO response = new ReservationResponseDTO();
        response.setId(saved.getId());
        response.setUserId(user.getId());
        response.setChargingPointId(chargingPoint.getId());
        response.setStartTime(saved.getStartTime());
        response.setEndTime(saved.getEndTime());
        response.setStatus(saved.getStatus());

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponseDTO>> getAllReservations() {
        List<ReservationResponseDTO> reservations = reservationRepository.findAll().stream().map(res -> {
            ReservationResponseDTO dto = new ReservationResponseDTO();
            dto.setId(res.getId());
            dto.setUserId(res.getUser().getId());
            dto.setChargingPointId(res.getChargingPoint().getId());
            dto.setStartTime(res.getStartTime());
            dto.setEndTime(res.getEndTime());
            dto.setStatus(res.getStatus());
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(reservations);
    }
}
