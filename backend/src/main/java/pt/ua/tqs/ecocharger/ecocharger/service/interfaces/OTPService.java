package pt.ua.tqs.ecocharger.ecocharger.service.interfaces;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.ua.tqs.ecocharger.ecocharger.models.OTPCode;
import pt.ua.tqs.ecocharger.ecocharger.models.Reservation;

public interface OTPService {
  public OTPCode generateOtp(Long reservationId);
  public boolean validateOtp(String code, Reservation reservation);   
}
