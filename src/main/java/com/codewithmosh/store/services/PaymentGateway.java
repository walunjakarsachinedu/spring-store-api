package com.codewithmosh.store.services;

import com.codewithmosh.store.entities.Order;

import java.util.Optional;

public interface PaymentGateway {
  CheckoutSession getCheckoutSession(Order order) throws PaymentException;
  Optional<PaymentResult> parseWebhookResponse(WebhookRequest request) throws PaymentException;
}
