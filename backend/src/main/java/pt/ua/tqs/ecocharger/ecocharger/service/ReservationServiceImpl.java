package pt.ua.tqs.ecocharger.ecocharger.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import pt.ua.tqs.ecocharger.ecocharger.dto.ChargingPointDTO;
import pt.ua.tqs.ecocharger.ecocharger.dto.ChargingStationDTO;
import pt.ua.tqs.ecocharger.ecocharger.dto.ConnectorDTO;
import pt.ua.tqs.ecocharger.ecocharger.dto.ReservationRequestDTO;
import pt.ua.tqs.ecocharger.ecocharger.dto.ReservationResponseDTO;
import pt.ua.tqs.ecocharger.ecocharger.models.*;
import pt.ua.tqs.ecocharger.ecocharger.repository.*;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.ReservationService;

import java.time.LocalDateTime;
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

    long durationMinutes = java.time.Duration.between(request.getStartTime(), request.getEndTime()).toMinutes();
    if (durationMinutes < 15) {
        throw new IllegalArgumentException("Reservation must be at least 15 minutes long");
    }
    if (durationMinutes > 240) {
        throw new IllegalArgumentException("Reservation cannot exceed 4 hours");
    }

    User user = userRepository
        .findById(request.getUserId())
        .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

    ChargingPoint chargingPoint = chargingPointRepository
        .findById(request.getChargingPointId())
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
    reservation.setStatus(ReservationStatus.TO_BE_USED);

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
    ChargingPoint cp = res.getChargingPoint();
    ChargingStation station = cp.getChargingStation();

    ChargingStationDTO stationDTO = new ChargingStationDTO();
    stationDTO.setId(station.getId());
    stationDTO.setCityName(station.getCityName());
    stationDTO.setAddress(station.getAddress());
    stationDTO.setLatitude(station.getLatitude());
    stationDTO.setLongitude(station.getLongitude());

    ChargingPointDTO chargingPointDTO = new ChargingPointDTO();
    chargingPointDTO.setId(cp.getId());
    chargingPointDTO.setBrand(cp.getBrand());
    chargingPointDTO.setAvailable(cp.isAvailable());
    chargingPointDTO.setPricePerKWh(cp.getPricePerKWh());
    chargingPointDTO.setPricePerMinute(cp.getPricePerMinute());
    chargingPointDTO.setChargingStation(stationDTO);
    chargingPointDTO.setConnectors(
        cp.getConnectors().stream()
            .map(
                conn -> {
                  ConnectorDTO cDto = new ConnectorDTO();
                  cDto.setId(conn.getId());
                  cDto.setConnectorType(conn.getConnectorType());
                  cDto.setRatedPowerKW(conn.getRatedPowerKW());
                  cDto.setVoltageV(conn.getVoltageV());
                  cDto.setCurrentA(conn.getCurrentA());
                  return cDto;
                })
            .collect(Collectors.toList()));

    ReservationResponseDTO dto = new ReservationResponseDTO();
    dto.setId(res.getId());
    dto.setUserId(res.getUser().getId());
    dto.setChargingPoint(chargingPointDTO);
    dto.setStartTime(res.getStartTime());
    dto.setEndTime(res.getEndTime());
    dto.setStatus(res.getStatus());

    return dto;
  }

  @Override
  public List<ReservationResponseDTO> getReservationsByUserId(Long userId) {
    List<Reservation> reservations = reservationRepository.findByUserId(userId);
    return reservations.stream().map(this::mapToDTO).collect(Collectors.toList());
  }

  @Override
  public List<ReservationResponseDTO> getActiveReservationsByChargingPointId(Long chargingPointId) {
    LocalDateTime now = LocalDateTime.now();
    List<Reservation> reservations = reservationRepository.findByChargingPointIdAndEndTimeAfter(chargingPointId, now);

    return reservations.stream().map(this::mapToDTO).collect(Collectors.toList());
  }
}
