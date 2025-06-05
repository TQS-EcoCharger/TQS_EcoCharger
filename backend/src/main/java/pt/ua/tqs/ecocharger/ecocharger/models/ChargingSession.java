package pt.ua.tqs.ecocharger.ecocharger.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "charging_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChargingSession {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(optional = true)
  @JoinColumn(name = "reservation_id", nullable = true)
  private Reservation reservation;

  @ManyToOne(optional = false)
  @JoinColumn(name = "charging_point_id", nullable = false)
  private ChargingPoint chargingPoint;

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(optional = false)
  @JoinColumn(name = "car_id", nullable = false)
  private Car car;

  @Column(name = "start_time", nullable = false)
  private LocalDateTime startTime;

  @Column(name = "end_time")
  private LocalDateTime endTime;

  @Column(name = "duration_minutes")
  private Long durationMinutes;

  @Column(name = "total_cost")
  private Double totalCost;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private ChargingStatus status;

  @Column(name = "initial_battery_level", nullable = false)
  private Double initialBatteryLevel;
}
