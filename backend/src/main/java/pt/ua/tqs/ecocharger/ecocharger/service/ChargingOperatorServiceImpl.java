package pt.ua.tqs.ecocharger.ecocharger.service;

import org.springframework.stereotype.Service;

import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.ChargingOperatorService;
import pt.ua.tqs.ecocharger.ecocharger.utils.NotFoundException;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingOperator;
import pt.ua.tqs.ecocharger.ecocharger.repository.ChargingOperatorRepository;

@Service
public class ChargingOperatorServiceImpl implements ChargingOperatorService {

  private final ChargingOperatorRepository chargingOperatorRepository;

  public ChargingOperatorServiceImpl(ChargingOperatorRepository chargingOperatorRepository) {
    this.chargingOperatorRepository = chargingOperatorRepository;
  }

  @Override
  public ChargingOperator getChargingOperatorById(Long id) {
    return chargingOperatorRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("Charging Operator not found with id: " + id));
  }

  @Override
  public boolean chargingOperatorExists(Long id) {
    return chargingOperatorRepository.existsById(id);
  }
}
