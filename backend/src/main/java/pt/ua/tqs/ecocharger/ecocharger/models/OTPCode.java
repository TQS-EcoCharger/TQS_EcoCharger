package pt.ua.tqs.ecocharger.ecocharger.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "OTP_code")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OTPCode {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String code;

  @Column(nullable = false)
  private LocalDateTime expirationTime;

  @OneToOne
  @JoinColumn(name = "reservation_id", nullable = false)
  private Reservation reservation;
}
