package pt.ua.tqs.ecocharger.ecocharger.dto;

public class OtpValidationRequestDTO {
    private String otp;
    private Long chargingPointId;

    public OtpValidationRequestDTO() {}

    public OtpValidationRequestDTO(String otp, Long chargingPointId) {
        this.otp = otp;
        this.chargingPointId = chargingPointId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Long getChargingPointId() {
        return chargingPointId;
    }

    public void setChargingPointId(Long chargingPointId) {
        this.chargingPointId = chargingPointId;
    }
}
