package pt.ua.tqs.ecocharger.ecocharger.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import pt.ua.tqs.ecocharger.ecocharger.dto.BalanceTopUpRequest;
import pt.ua.tqs.ecocharger.ecocharger.models.Car;
import pt.ua.tqs.ecocharger.ecocharger.models.Driver;
import pt.ua.tqs.ecocharger.ecocharger.service.StripeService;
import pt.ua.tqs.ecocharger.ecocharger.service.interfaces.DriverService;
import pt.ua.tqs.ecocharger.ecocharger.utils.NotFoundException;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;


@Controller
@RequestMapping("/api/v1/driver")
public class DriverController {

  private final DriverService driverService;

  private final StripeService stripeService;

  public DriverController(DriverService driverService, StripeService stripeService) {
      this.driverService = driverService;
      this.stripeService = stripeService;
  }

  @GetMapping("/")
  public ResponseEntity<List<Driver>> getAllDrivers() {
    return ResponseEntity.ok(driverService.getAllDrivers());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Object> getDriverById(@PathVariable Long id) {
    try {
      Driver existingDriver = driverService.getDriverById(id);
      System.out.println("Driver found: " + existingDriver);
      return ResponseEntity.ok(existingDriver);
    } catch (NotFoundException e) {
      return ResponseEntity.status(404).body(e.getMessage());
    }
  }

  @PostMapping("/")
  public ResponseEntity<Object> createDriver(@RequestBody Driver driver) {
    try {
      Driver newDriver = driverService.createDriver(driver);
      return ResponseEntity.ok(newDriver);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(400).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(500).body("An error occurred while creating the driver.");
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<Object> updateDriver(@PathVariable Long id, @RequestBody Driver driver) {
    try {
      Driver updatedDriver = driverService.updateDriver(id, driver);
      return ResponseEntity.ok(updatedDriver);
    } catch (NotFoundException e) {
      return ResponseEntity.status(404).body(e.getMessage());
    }
  }

  @PatchMapping("{id}/cars/")
  public ResponseEntity<Object> addCarToDriver(@PathVariable Long id, @RequestBody Car car) {
    try {
      System.out.println("Adding car to driver with ID: " + id);
      System.out.println("Car details: " + car);
      Driver updatedDriver = driverService.addCarToDriver(id, car);
      return ResponseEntity.ok(updatedDriver);
    } catch (NotFoundException e) {
      return ResponseEntity.status(404).body(e.getMessage());
    }
  }

  @DeleteMapping("{id}/cars/{carId}")
  public ResponseEntity<Object> removeCarFromDriver(
      @PathVariable Long id, @PathVariable Long carId) {
    try {
      Driver existingDriver = driverService.removeCarFromDriver(id, carId);
      return ResponseEntity.ok(existingDriver);
    } catch (NotFoundException e) {
      return ResponseEntity.status(404).body(e.getMessage());
    }
  }

  @PatchMapping("{id}/cars/{carId}")
  public ResponseEntity<Object> editCarFromDriver(
      @PathVariable Long id, @PathVariable Long carId, @RequestBody Car car) {
    try {
      Driver updatedDriver = driverService.editCarFromDriver(id, carId, car);
      return ResponseEntity.ok(updatedDriver);
    } catch (NotFoundException e) {
      return ResponseEntity.status(404).body(e.getMessage());
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> deleteDriver(@PathVariable Long id) {
    try {
      driverService.deleteDriver(id);
      return ResponseEntity.noContent().build();
    } catch (NotFoundException e) {
      return ResponseEntity.status(404).body(e.getMessage());
    }
  }

    @PostMapping("/{id}/balance")
    public ResponseEntity<Object> topUpBalance(@PathVariable Long id, @RequestBody BalanceTopUpRequest request) {
        try {
            if (request.getAmount() == null || request.getAmount() <= 0) {
                return ResponseEntity.badRequest().body(Map.of("error", "Amount must be greater than 0"));
            }

            long amountInCents = Math.round(request.getAmount() * 100);

            SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                .setQuantity(1L)
                .setPriceData(
                    SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency("eur")
                        .setUnitAmount(amountInCents)
                        .setProductData(
                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName("Balance Top-up for Driver #" + id)
                                .build()
                        )
                        .build()
                )
                .build();

            SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:5000/payment-success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("http://localhost:5000/cancel")
                .addLineItem(lineItem)
                .putMetadata("userId", String.valueOf(id))
                .putMetadata("amount", String.valueOf(request.getAmount()))
                .build();

            Session session = Session.create(params);

            return ResponseEntity.ok(Map.of(
                "sessionId", session.getId(),
                "url", session.getUrl()
            ));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/checkout-success")
    public ResponseEntity<Object> finalizeTopUp(@RequestParam("session_id") String sessionId) {
        try {
            Session session = Session.retrieve(sessionId);
            Long driverId = Long.parseLong(session.getMetadata().get("userId"));
            Double amount = Double.parseDouble(session.getMetadata().get("amount"));

            driverService.addBalanceToDriver(driverId, amount);
            return ResponseEntity.ok(Map.of("status", "success"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

  @PatchMapping("/{id}/balance/add")
  public ResponseEntity<Object> manuallyAddBalance(
      @PathVariable Long id,
      @RequestBody BalanceTopUpRequest request
  ) {
      try {
          driverService.addBalanceToDriver(id, request.getAmount());
          return ResponseEntity.ok(Map.of("status", "balance updated"));
      } catch (NotFoundException e) {
          return ResponseEntity.status(404).body(e.getMessage());
      }
  }
}
