package com.codewithmosh.store.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CheckoutResponse {
  private final Long orderId;
  private final String checkoutUrl;
}
