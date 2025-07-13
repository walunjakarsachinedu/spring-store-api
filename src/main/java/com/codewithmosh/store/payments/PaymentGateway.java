package com.codewithmosh.store.payments;

import com.codewithmosh.store.orders.entities.Order;
import com.codewithmosh.store.carts.CheckoutSession;
import com.codewithmosh.store.payments.dtos.WebhookRequest;

import java.util.Optional;

public interface PaymentGateway {
  CheckoutSession getCheckoutSession(Order order) throws PaymentException;
  Optional<PaymentResult> parseWebhookResponse(WebhookRequest request) throws PaymentException;
}
