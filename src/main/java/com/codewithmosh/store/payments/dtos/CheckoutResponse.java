package com.codewithmosh.store.payments.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CheckoutResponse {
  private final Long orderId;
  private final String checkoutUrl;
}
