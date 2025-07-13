package com.codewithmosh.store.payments.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class CheckoutRequest {
  @NotNull(message = "cartId is required")
  private UUID cartId;
}
