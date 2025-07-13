package com.codewithmosh.store.payments;

import com.codewithmosh.store.orders.entities.Order;
import com.codewithmosh.store.orders.entities.OrderItem;
import com.codewithmosh.store.orders.entities.OrderStatus;
import com.codewithmosh.store.orders.OrderRepository;
import com.codewithmosh.store.carts.CheckoutSession;
import com.codewithmosh.store.payments.dtos.WebhookRequest;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;


@RequiredArgsConstructor
@Service
public class StripePaymentService implements PaymentGateway {
  private final OrderRepository orderRepository;

  @Value("${websiteUrl}")
  private String websiteUrl;

  @Value("${stripe.webhookSecretKey}")
  private String webhookSecretKey;

  @Override
  public CheckoutSession getCheckoutSession(Order order) throws PaymentException {
    try {
      var sessionParams = SessionCreateParams.builder()
        .setMode(SessionCreateParams.Mode.PAYMENT)
        .setSuccessUrl(websiteUrl + "/checkout-success?orderId=" + order.getId())
        .setCancelUrl(websiteUrl + "/checkout-cancel");

      order.getItems().forEach(item -> sessionParams.addLineItem(getLineItem(item)));

      var stripeSession = Session.create(sessionParams.build());
      return new CheckoutSession(stripeSession.getUrl());
    } catch (StripeException exp) {
      throw new PaymentException();
    }
  }

  @Override
  public Optional<PaymentResult> parseWebhookResponse(WebhookRequest request) {
    var signature = request.headers().get("stripe-signature");

    try {
      var event = Webhook.constructEvent(request.payload(), signature, webhookSecretKey);

      return switch (event.getType()) {
        case "payment_intent.succeeded" ->
          Optional.of(new PaymentResult(extractOrderId(event), OrderStatus.PAID));
        case "payment_method.failed" ->
          Optional.of(new PaymentResult(extractOrderId(event), OrderStatus.FAILED));
        default -> Optional.empty();
      };

    }
    catch (SignatureVerificationException e) {
      throw new PaymentException("Invalid webhook signature, so unable to deserialize event data");
    }

  }

  private Long extractOrderId(Event event) throws SignatureVerificationException {
    var stripeObject = event.getDataObjectDeserializer().getObject().orElseThrow(
      () -> new PaymentException("Could not deserialize Stripe event. Check the SDK and API version.")
    );
    var paymentIntent = (PaymentIntent) stripeObject;
    return Long.valueOf(paymentIntent.getMetadata().get("order_id"));
  }

  private SessionCreateParams.LineItem getLineItem(OrderItem item) {
    return SessionCreateParams.LineItem.builder()
      .setQuantity(Long.valueOf(item.getQuantity()))
      .setPriceData(getPriceData(item))
      .build();
  }

  private SessionCreateParams.LineItem.PriceData getPriceData(OrderItem item) {
    return SessionCreateParams.LineItem.PriceData.builder()
      .setCurrency("usd")
      .setUnitAmount(item.getProduct().getPrice().longValue() * 1000)
      .setProductData(
        getProductData(item)
      )
      .build();
  }

  private SessionCreateParams.LineItem.PriceData.ProductData getProductData(OrderItem item) {
    return SessionCreateParams.LineItem.PriceData.ProductData.builder()
      .setName(item.getProduct().getName())
      .build();
  }
}
