package pt.ua.tqs.ecocharger.ecocharger.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

  public StripeService(@Value("${stripe.secret}") String stripeSecretKey) {
    Stripe.apiKey = stripeSecretKey;
  }

  public PaymentIntent createTopUpIntent(Long driverId, String email, Double amount)
      throws StripeException {
    PaymentIntentCreateParams params =
        PaymentIntentCreateParams.builder()
            .setAmount((long) (amount * 100))
            .setCurrency("eur")
            .setReceiptEmail(email)
            .putMetadata("driverId", driverId.toString())
            .build();

    return PaymentIntent.create(params);
  }
}
