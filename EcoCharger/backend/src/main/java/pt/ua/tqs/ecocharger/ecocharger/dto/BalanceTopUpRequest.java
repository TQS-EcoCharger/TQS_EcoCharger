package pt.ua.tqs.ecocharger.ecocharger.dto;

import lombok.Data;

@Data
public class BalanceTopUpRequest {
  private Double amount;
  private boolean simulateSuccess;
}
