package pt.ua.tqs.ecocharger.ecocharger.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import app.getxray.xray.junit.customjunitxml.annotations.Requirement;
import pt.ua.tqs.ecocharger.ecocharger.models.OTPCode;
import pt.ua.tqs.ecocharger.ecocharger.models.Reservation;
import pt.ua.tqs.ecocharger.ecocharger.models.ReservationStatus;
import pt.ua.tqs.ecocharger.ecocharger.repository.OTPCodeRepository;
import pt.ua.tqs.ecocharger.ecocharger.repository.ReservationRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OTPServiceImplTest {

    private OTPCodeRepository otpCodeRepository;
    private ReservationRepository reservationRepository;
    private OTPServiceImpl otpService;

    private Reservation mockReservation;

    @BeforeEach
    void setup() {
        otpCodeRepository = mock(OTPCodeRepository.class);
        reservationRepository = mock(ReservationRepository.class);
        otpService = new OTPServiceImpl(otpCodeRepository, reservationRepository);

        mockReservation = new Reservation();
        mockReservation.setId(1L);
        mockReservation.setStatus(ReservationStatus.TO_BE_USED);
    }

    @Test
    @Requirement("ET-43")
    @DisplayName("Generate OTP successfully when reservation is TO_BE_USED")
    void generateOtpSuccess() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(mockReservation));
        when(otpCodeRepository.save(any(OTPCode.class))).thenAnswer(i -> i.getArgument(0));

        OTPCode otp = otpService.generateOtp(1L);

        assertThat(otp).isNotNull();
        assertThat(otp.getCode()).hasSize(6).matches("\\d{6}");
        assertThat(otp.getExpirationTime()).isAfter(LocalDateTime.now());

        ArgumentCaptor<OTPCode> captor = ArgumentCaptor.forClass(OTPCode.class);
        verify(otpCodeRepository).save(captor.capture());
        assertThat(captor.getValue().getReservation()).isEqualTo(mockReservation);
    }

    @Test
    @Requirement("ET-43")
    @DisplayName("Throws exception when reservation not found")
    void generateOtpReservationNotFound() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> otpService.generateOtp(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Reservation not found");
    }

    @Test
    @Requirement("ET-43")
    @DisplayName("Throws exception if reservation is not TO_BE_USED")
    void generateOtpInvalidReservationStatus() {
        mockReservation.setStatus(ReservationStatus.USED);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(mockReservation));

        assertThatThrownBy(() -> otpService.generateOtp(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("OTP can only be generated");
    }

    @Test
    @Requirement("ET-43")
    @DisplayName("Deletes existing OTP if one already exists for reservation")
    void generateOtpDeletesExistingOtp() {
        OTPCode existingOtp = new OTPCode(10L, "123456", LocalDateTime.now().plusMinutes(5), mockReservation);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(mockReservation));
        when(otpCodeRepository.findByReservation(mockReservation)).thenReturn(Optional.of(existingOtp));
        when(otpCodeRepository.save(any(OTPCode.class))).thenAnswer(i -> i.getArgument(0));

        otpService.generateOtp(1L);

        verify(otpCodeRepository).delete(existingOtp);
        verify(otpCodeRepository).save(any(OTPCode.class));
    }

    @Test
    @Requirement("ET-43")
    @DisplayName("Returns true if OTP is valid and not expired")
    void validateOtpSuccess() {
        OTPCode validOtp = new OTPCode(1L, "999999", LocalDateTime.now().plusMinutes(5), mockReservation);
        when(otpCodeRepository.findByCodeAndReservation("999999", mockReservation))
                .thenReturn(Optional.of(validOtp));

        boolean result = otpService.validateOtp("999999", mockReservation);

        assertThat(result).isTrue();
    }

    @Test
    @Requirement("ET-43")
    @DisplayName("Returns false if OTP is expired")
    void validateOtpExpired() {
        OTPCode expiredOtp = new OTPCode(1L, "111111", LocalDateTime.now().minusMinutes(1), mockReservation);
        when(otpCodeRepository.findByCodeAndReservation("111111", mockReservation))
                .thenReturn(Optional.of(expiredOtp));

        boolean result = otpService.validateOtp("111111", mockReservation);

        assertThat(result).isFalse();
    }

    @Test
    @Requirement("ET-43")
    @DisplayName("Returns false if OTP not found for reservation")
    void validateOtpNotFound() {
        when(otpCodeRepository.findByCodeAndReservation("000000", mockReservation))
                .thenReturn(Optional.empty());

        boolean result = otpService.validateOtp("000000", mockReservation);

        assertThat(result).isFalse();
    }
}
