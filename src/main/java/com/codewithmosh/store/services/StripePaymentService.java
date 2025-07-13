package com.codewithmosh.store.services;

import com.codewithmosh.store.entities.Order;
import com.codewithmosh.store.entities.OrderItem;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class StripePaymentService implements PaymentGateway {
  @Value("${websiteUrl}")
  private String websiteUrl;

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
